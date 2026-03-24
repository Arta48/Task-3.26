package ru.vsu.cs.course1;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class MainFrame extends JFrame {

    private int boardSize = 8; // По умолчанию 8 ферзей
    private List<Integer> queensPlacement = null;
    private final ChessBoardPanel boardPanel;

    public MainFrame() {
        setTitle("Расстановка ферзей (Стек)");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boardPanel = new ChessBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();

        JButton btnLoad = new JButton("Загрузить N из файла");
        JButton btnStandard = new JButton("Решить (Стандартный Стек)");
        JButton btnCustom = new JButton("Решить (Самописный Стек)");

        controlPanel.add(btnLoad);
        controlPanel.add(btnStandard);
        controlPanel.add(btnCustom);
        add(controlPanel, BorderLayout.SOUTH);

        // Обработчики кнопок

        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File("."));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner scanner = new Scanner(fileChooser.getSelectedFile())) {
                    if (scanner.hasNextInt()) {
                        boardSize = scanner.nextInt();
                        queensPlacement = null;
                        boardPanel.repaint();
                        JOptionPane.showMessageDialog(this, "Размер доски установлен: " + boardSize);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка чтения файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnStandard.addActionListener(e -> {
            // Используем стандартный ArrayDeque
            Deque<Integer> standardStack = new ArrayDeque<>();
            long startTime = System.nanoTime();
            queensPlacement = TaskLogic.solve(standardStack, boardSize);
            long endTime = System.nanoTime();

            boardPanel.repaint();
            showResultDialog("Стандартный стек (ArrayDeque)", endTime - startTime);
        });

        btnCustom.addActionListener(e -> {
            // Используем SimpleDeque
            Deque<Integer> myStack = new SimpleDeque<>();
            long startTime = System.nanoTime();
            queensPlacement = TaskLogic.solve(myStack, boardSize);
            long endTime = System.nanoTime();

            boardPanel.repaint();
            showResultDialog("Самописный стек (SimpleDeque)", endTime - startTime);
        });
    }

    private void showResultDialog(String stackType, long timeNano) {
        if (queensPlacement.size() == boardSize) {
            JOptionPane.showMessageDialog(this,
                    "Решение найдено!\nРеализация: " + stackType + "\nВремя: " + (timeNano / 1_000_000.0) + " мс");
        } else {
            JOptionPane.showMessageDialog(this, "Решение не найдено для N = " + boardSize);
        }
    }

    // Внутренний класс для отрисовки доски
    private class ChessBoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width, height) / boardSize;

            int xOffset = (width - cellSize * boardSize) / 2;
            int yOffset = (height - cellSize * boardSize) / 2;

            // Рисуем клетчатую доску
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    if ((row + col) % 2 == 0) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.fillRect(xOffset + col * cellSize, yOffset + row * cellSize, cellSize, cellSize);
                }
            }

            // Рисуем ферзей
            if (queensPlacement != null && queensPlacement.size() == boardSize) {
                g.setColor(Color.RED);
                for (int row = 0; row < queensPlacement.size(); row++) {
                    int col = queensPlacement.get(row);
                    g.fillOval(xOffset + col * cellSize + cellSize / 4,
                            yOffset + row * cellSize + cellSize / 4,
                            cellSize / 2, cellSize / 2);
                }
            }
        }
    }
}