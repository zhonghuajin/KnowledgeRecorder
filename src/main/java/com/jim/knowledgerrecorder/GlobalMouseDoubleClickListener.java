package com.jim.knowledgerrecorder;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalMouseDoubleClickListener implements NativeMouseListener {

    private long lastClickTime = 0;
    private int clickCount = 0;
    private Robot robot;

    public GlobalMouseDoubleClickListener() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime <= 500) { // 双击时间间隔阈值（毫秒）
            clickCount++;
            if (clickCount == 2) {
                System.out.println("检测到双击，执行复制操作...");
                performCopy();
                clickCount = 0; // 重置点击计数
            }
        } else {
            clickCount = 1;
        }
        lastClickTime = currentTime;
    }

    private void performCopy() {
        // 模拟按下Ctrl + C
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);

        // 模拟释放键
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        // 不需要实现
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        // 不需要实现
    }

    public static void main(String[] args) {
        try {
            // 禁用JNativeHook的默认日志输出
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            // 注册全局鼠标监听器
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(new GlobalMouseDoubleClickListener());
            System.out.println("全局双击监听已启动。按下Ctrl + C以复制选中的内容。");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}