import cv2
import numpy as np

def image_to_gcode(image_path, output_path, feedrate=1000, epsilon=0.5, max_width=105, max_height=148.5):
    # Încărcarea imaginii
    image = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    height, width = image.shape

    # Calcularea factorului de scalare pentru a încadra desenul pe jumătate de foaie A4
    scale_factor = min(max_width / width, max_height / height)

    # Aplicarea threshold-ului pentru a obține o imagine binară
    _, binary_image = cv2.threshold(image, 128, 255, cv2.THRESH_BINARY_INV)

    # Detectarea contururilor cu simplificare
    contours, _ = cv2.findContours(binary_image, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # Inițializarea fișierului G-code
    gcode_lines = [
        "G21 ; Set units to mm",
        "G90 ; Absolute positioning"
    ]

    # Procesarea fiecărui contur detectat
    for contour in contours:
        if len(contour) > 0:
            # Simplificarea conturului
            simplified_contour = cv2.approxPolyDP(contour, epsilon, closed=True)

            # Ridicare pix înainte de deplasare
            gcode_lines.append("M5 ; Tool off")

            # Primul punct - mișcare rapidă către începutul conturului
            x, y = simplified_contour[0][0]
            gcode_lines.append(f"G0 X{x * scale_factor:.2f} Y{(height - y) * scale_factor:.2f}")

            # Coborâre pix pentru desen
            gcode_lines.append("M3 ; Tool on")

            # Adăugarea punctelor din contur
            for point in simplified_contour:
                x, y = point[0]
                gcode_lines.append(f"G01 X{x * scale_factor:.2f} Y{(height - y) * scale_factor:.2f} F{feedrate}")

            # Închiderea conturului
            x, y = simplified_contour[0][0]
            gcode_lines.append(f"G01 X{x * scale_factor:.2f} Y{(height - y) * scale_factor:.2f} F{feedrate}")

            # Ridicare pix la sfârșitul conturului
            gcode_lines.append("M5 ; Tool off")

    # Închiderea programului
    gcode_lines.append("M30 ; End of program")

    # Salvarea fișierului G-code
    with open(output_path, "w") as file:
        file.write("\n".join(gcode_lines))

    print(f"Fișierul G-code a fost generat: {output_path}")

# Exemplu de utilizare
image_to_gcode(image_path="poza5.png", output_path="output.txt", epsilon=2.0)

