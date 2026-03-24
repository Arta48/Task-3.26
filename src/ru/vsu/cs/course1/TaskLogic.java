package ru.vsu.cs.course1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TaskLogic {

    public static List<Integer> solve(Deque<Integer> stack, int boardSize) {
        stack.clear();
        int currentCol = 0;

        // Пока не поставили ферзей на все строки
        while (stack.size() < boardSize) {
            boolean placed = false;

            // Ищем безопасную колонку в текущей строке
            while (currentCol < boardSize) {
                if (isSafe(stack, currentCol)) {
                    stack.push(currentCol); // Ставим ферзя (кладём в стек)
                    placed = true;
                    currentCol = 0; // Для следующей строки начинаем с нулевой колонки
                    break;
                }
                currentCol++;
            }

            // Если не удалось поставить ферзя в текущей строке - возвращаемся назад (Backtrack)
            if (!placed) {
                if (stack.isEmpty()) {
                    break; // Решений нет
                }
                // Извлекаем ферзя с предыдущей строки и продолжаем поиск со следующей колонки
                currentCol = stack.pop() + 1;
            }
        }

        // Переводим стек в список для удобства отрисовки
        List<Integer> result = new ArrayList<>(stack);
        // Так как стек выдает элементы с конца (LIFO), переворачиваем список,
        // чтобы индекс списка соответствовал номеру строки сверху вниз.
        Collections.reverse(result);
        return result;
    }

    // Проверка, бьют ли уже поставленные ферзи (в стеке) новую позицию.
    private static boolean isSafe(Deque<Integer> stack, int newCol) {
        int newRow = stack.size(); // Строка, на которую пытаемся поставить
        int r = newRow - 1;

        // Перебираем ферзей в стеке сверху вниз (от предыдущей строки к нулевой)
        for (Integer c : stack) {
            if (c == newCol) {
                return false; // Совпадает колонка
            }
            if (Math.abs(c - newCol) == Math.abs(r - newRow)) {
                return false; // Совпадает диагональ
            }
            r--;
        }
        return true;
    }
}