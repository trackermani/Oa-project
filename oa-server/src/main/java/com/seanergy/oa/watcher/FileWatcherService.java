package com.seanergy.oa.watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.seanergy.oa.config.FileWatcherConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 서류관리 폴더를 감시하여 새 파일 도착 시 로그를 남긴다.
 * 실제 검증 트리거는 API 호출 방식으로 처리 (Phase 1).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileWatcherService {

    private final FileWatcherConfig config;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void startWatching() {
        String watchDir = config.getWatchDir();
        Path path = Paths.get(watchDir);

        if (!Files.exists(path)) {
            log.warn("[FileWatcher] 감시 경로가 존재하지 않습니다: {}", watchDir);
            return;
        }

        log.info("[FileWatcher] 폴더 감시 시작: {}", watchDir);

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    String fileName = event.context().toString();
                    if (isSupportedFile(fileName)) {
                        log.info("[FileWatcher] 새 파일 감지: {}", fileName);
                        // Phase 1: 로그만 남김
                        // Phase 2: 자동으로 파이프라인 트리거
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            log.error("[FileWatcher] 감시 중 오류: {}", e.getMessage());
        }
    }

    private boolean isSupportedFile(String fileName) {
        String lower = fileName.toLowerCase();
        return Arrays.stream(config.getSupportedExtensions())
                .anyMatch(ext -> lower.endsWith("." + ext));
    }
}
