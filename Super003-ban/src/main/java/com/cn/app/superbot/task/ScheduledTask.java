package com.cn.app.superbot.task;

import com.cn.app.superbot.entity.MiniProgramUser;
import com.cn.app.superbot.mapper.MiniProgramUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The type Scheduled task.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTask {

    /**
     * The Mini program user mapper.
     */
    private final MiniProgramUserMapper mapper;

    /**
     * Execute task.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        try {
            mapper.update(new MiniProgramUser().setShare(0L).setVideo(10L), null);
        } catch (Exception e) {
            log.error("更新分享字段失败");
        }
    }

}
