package com.example.myapplication;

import java.util.Arrays;

public class RotateTorus {
    // Толщина бублика
    private final float R1 = 0.5f;

    // Радиус бублика
    private final float R2 = 2;

    // FOV
    private final float K1 = 25;

    // Дистанция от зрителя
    private final float K2 = 5;

    // Шаг сетки бублика (большой/внешней окружности)
    private final float alpha_step = 0.1f; // 0.07

    // Шаг сетки (малой/внутренней окружности)
    private final float beta_step = 0.1f; // 0.04

    // ширина/высота изображения
    private final int width = 40;
    private final int height = 30;

    // интенсивность света
    private final char[] luminance_levels = {
            '.', ',', '-',
            '~', ':', ';',
            '=', '!', '*',
            '#', '$', '@'
    };

    public String getTorus(float x_offset, float y_offset, float rot_x, float rot_y) {
        float cos_A = (float) Math.cos(rot_x);
        float sin_A = (float) Math.sin(rot_x);

        float cos_B = (float) Math.cos(rot_y);
        float sin_B = (float) Math.sin(rot_y);

        char[][] output = new char[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                output[i][j] = ' ';

        float[][] zbuffer = new float[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                zbuffer[i][j] = 0.0f;

        for (float alpha = 0; alpha < 6.28f; alpha += this.alpha_step) {
            float cos_alpha = (float) Math.cos(alpha);
            float sin_alpha = (float) Math.sin(alpha);

            for (float beta = 0; beta < 6.28f; beta += this.beta_step) {
                float cos_beta = (float) Math.cos(beta);
                float sin_beta = (float) Math.sin(beta);

                float circle_x = this.R2 + this.R1 * cos_alpha;
                float circle_y = this.R1 * sin_alpha;

                float x = x_offset + circle_x * (cos_beta * cos_B - sin_beta * sin_A * sin_B) - circle_y * cos_A * sin_B;
                float y = y_offset - circle_x * sin_beta * cos_A + circle_y * sin_A;
                float z = K2 + circle_x * (cos_beta * sin_B + sin_beta * sin_A * cos_B) + circle_y * cos_A * cos_B;
                float ooz = this.K1 / (this.K2 + z);

                int x_proj = width / 2 + (int) (x_offset + ooz * x);
                int y_proj = height / 2 - (int) (y_offset + ooz * y);

                if (x_proj < 0 || x_proj > width - 1) continue;
                if (y_proj < 0 || y_proj > height - 1) continue;

                float L = cos_alpha * (sin_beta * (sin_A * cos_B - cos_A) - cos_beta * sin_B) + sin_alpha * (sin_A - cos_A * cos_B);

                if (L > 0) {
                    if (ooz > zbuffer[y_proj][x_proj]) {
                        zbuffer[y_proj][x_proj] = ooz;
                        int luminance_index = (int) (L * 8);
                        output[y_proj][x_proj] = luminance_levels[luminance_index % 12];
                    }
                }
            }
        }
        String result = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result += output[i][j];
            }
            result += '\n';
        }

        return result;
    }
}