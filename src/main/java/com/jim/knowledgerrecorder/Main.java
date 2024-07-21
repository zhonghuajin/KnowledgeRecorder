package com.jim.knowledgerrecorder;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static String clipboardContent = "";

    public static void main(String[] args) {
        ClipboardWatcher clipboardWatcher = new ClipboardWatcher();
        clipboardWatcher.startMonitoring();
        GlobalKeyListener.saveSignalMonitor();
    }

    public static void saveClipBoardContern() {

        // 判断剪贴板内容是否为空
        if (clipboardContent == null || clipboardContent.isEmpty()) {
            System.out.println("剪贴板内容为空。");
            return;
        }

        // 弹出对话框，提示用户输入文件名
        String fileName = JOptionPane.showInputDialog("请输入文件名：");

        // 如果用户取消输入则退出
        if (fileName == null) {
            clipboardContent = "";
            return;
        }

        // 以uuid为文件名保存到本地
        File file = new File("D:\\BaiduSyncdisk\\技术\\" + fileName + ".txt");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(clipboardContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "剪贴板内容已保存到文件：" + file.getAbsolutePath());

        // 清空剪贴板内容
        clipboardContent = "";
    }
}
