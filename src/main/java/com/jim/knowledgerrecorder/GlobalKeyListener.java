package com.jim.knowledgerrecorder;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.logging.Logger;
import java.util.logging.Level;

public class GlobalKeyListener implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // 检查是否同时按下 Ctrl, Shift, Alt 以及 A 键
        if ((e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0 &&
                (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0 &&
                (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0 &&
                e.getKeyCode() == NativeKeyEvent.VC_A) {
            System.out.println("组合键 Ctrl + Shift + Alt + A 被触发");
            ClipboardWatcher.setEnable(false);
            try {
                Main.saveClipBoardKnowledge();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ClipboardWatcher.setEnable(true);
        }

        // 检查是否同时按下 Ctrl, Shift, Alt 以及 X 键
        if ((e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0 &&
                (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0 &&
                e.getKeyCode() == NativeKeyEvent.VC_X) {
            System.out.println("组合键 Ctrl + Shift + X 被触发");
            try {
                Main.saveClipBoardWord();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // 可以在这里处理键释放事件
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // 通常此方法用于处理打字事件，此场景中不需要实现
    }

    public static void saveSignalMonitor() {
        try {
            // 设置日志级别，减少不必要的输出
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);
            logger.setUseParentHandlers(false);

            GlobalScreen.registerNativeHook();
            System.out.println("全局键盘监听器成功注册。");
        } catch (NativeHookException ex) {
            System.err.println("注册全局钩子时出现问题。");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        // 添加键盘事件监听器
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
    }
}