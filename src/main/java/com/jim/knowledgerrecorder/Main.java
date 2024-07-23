package com.jim.knowledgerrecorder;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Main {

    public static String clipboardContent = "";

    public static void main(String[] args) {
        ClipboardWatcher clipboardWatcher = new ClipboardWatcher();
        clipboardWatcher.startMonitoring();
        GlobalKeyListener.saveSignalMonitor();
    }

    public static void saveClipBoardKnowledge() {

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

        // 把文件名中的非法字符替换为下划线
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

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

    public static void saveClipBoardWord() {
        // 判断剪贴板内容是否为空
        if (clipboardContent == null || clipboardContent.isEmpty()) {
            System.out.println("剪贴板内容为空。");
            return;
        }

        // 通过Localdatetime获取系统当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(System.currentTimeMillis());

        File file = new File("D:\\BaiduSyncdisk\\英语生词\\" + date + ".txt" );

        // 如果文件夹不存在则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 追加写入
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(clipboardContent + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "剪贴板内容已保存到文件：" + file.getAbsolutePath());

        // 清空剪贴板内容
        clipboardContent = "";
    }
}
