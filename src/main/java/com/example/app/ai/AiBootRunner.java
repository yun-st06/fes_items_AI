package com.example.app.ai;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
// import org.springframework.stereotype.Component;

/**
 * 起動時にAIを叩くデモランナー。
 * デフォルトでは無効（@Profile("ai-boot")）なので本番起動に影響しない。
 * 有効化したい時だけ: --spring.profiles.active=ai-boot
 */
@Profile("ai-boot")
// @Component  // ← 使う時だけコメントを外す
public class AiBootRunner implements CommandLineRunner {

    private final AiService ai;

    public AiBootRunner(AiService ai) {
        this.ai = ai;
    }

    @Override
    public void run(String... args) throws Exception {
        String out = ai.askOnce("フェスに必須の持ち物を3つだけ、日本語で簡潔に教えて。");
        System.out.println("[AI RESULT] " + out);
    }
}
