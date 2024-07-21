package com.jim.knowledgerrecorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClipboardWatcher implements ClipboardOwner {

    private Clipboard systemClipboard;
    private ExecutorService executor;

    public ClipboardWatcher() {
        systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        executor = Executors.newSingleThreadExecutor();
    }

    public void startMonitoring() {
        executor.submit(() -> {
            Transferable contents = systemClipboard.getContents(this);
            gainOwnership(contents);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(200); // Sleep for a small delay to check for clipboard changes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Good practice to re-interrupt the thread
                }
            }
        });
    }

    private void gainOwnership(Transferable t) {
        systemClipboard.setContents(t, this);
    }


    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        Timer timer = new Timer("ClipboardWatcherTimer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Transferable newContents = clipboard.getContents(this);
                    processContents(newContents);
                    gainOwnership(newContents);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500); // 延迟 500 毫秒后尝试
    }

    private void processContents(Transferable newContents) {
        try {
            // 检查是否为字符串数据
            if (newContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String data = (String) newContents.getTransferData(DataFlavor.stringFlavor);
                if (data != null) {
                    updateFile(data);
                }
            }
            // 检查是否为图像数据
            else if (newContents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                BufferedImage image = (BufferedImage) newContents.getTransferData(DataFlavor.imageFlavor);
                if (image != null) {
                    processImage(image);
                }
            }
        } catch (UnsupportedFlavorException | IOException e) {
            System.err.println("访问剪贴板数据时出错: " + e.getMessage());
        }
    }

    /**
     * 将BufferedImage转换为Base64字符串
     * @param image 图像对象
     * @param format 图像格式（如 "png", "jpeg"）
     * @return Base64编码的字符串
     * @throws IOException
     */
    public static String encodeToString(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, format, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private void processImage(BufferedImage image) throws IOException {

    }


    private void updateFile(String data) {

        Main.clipboardContent = data;

    }

}