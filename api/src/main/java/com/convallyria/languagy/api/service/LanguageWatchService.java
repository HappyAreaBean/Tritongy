package com.convallyria.languagy.api.service;

import com.convallyria.languagy.api.HookedPlugin;
import com.convallyria.languagy.api.language.Language;
import com.convallyria.languagy.api.language.key.LanguageKey;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class LanguageWatchService {

    private final ScheduledExecutorService executorService;
    private final WatchService watchService;

    public LanguageWatchService(HookedPlugin hookedPlugin) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        final Path path = hookedPlugin.getFolder().toPath();
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        this.executorService = Executors.newScheduledThreadPool(1);
        final Runnable actualTask = () -> {
            try {
                System.out.println("running: " + Thread.currentThread());
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    final String context = String.valueOf(event.context());
                    final Optional<Language> language = Language.getFromKey(LanguageKey.of(context.replace(".yml", "")));
                    if (!language.isPresent()) return;

                    if (event.kind().equals(ENTRY_DELETE)) {
                        // Check if it's the fallback file, and if so, cancel the removal from the cache
                        // data cached to be available to use.
                        // The plugin should replace it on the next restart.
                        if (hookedPlugin.getFallback().getName().equals(context)) {
                            return;
                        }

                        hookedPlugin.removeCachedLanguage(language.get());
                        continue;
                    }

                    final File targetFile = new File(hookedPlugin.getFolder() + File.separator + context);
                    final YamlConfiguration config = YamlConfiguration.loadConfiguration(targetFile);
                    hookedPlugin.addCachedLanguage(language.get(), config);
                }

                if (!key.reset()) {
                    executorService.shutdown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        executorService.scheduleAtFixedRate(new Runnable() {
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            private Future<?> lastExecution;
            @Override
            public void run() {
                if (lastExecution != null && !lastExecution.isDone()) {
                    return;
                }
                lastExecution = executor.submit(actualTask);
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
