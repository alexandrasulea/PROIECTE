import serial
import time

# Deschide conexiunea serială
ser = serial.Serial('COM11', 57600)  # Asigură-te că folosești portul corect

# Așteaptă câteva secunde pentru a da timp Arduino-ului să se inițializeze
time.sleep(2)

# Citește fișierul G-code
with open("output.txt", "r") as file:
    gcode_lines = file.readlines()

# Trimite fiecare linie de G-code la Arduino
for line in gcode_lines:
    ser.write(line.encode())
    print(f"Trimis: {line}")
    # trimite linia în format bytes
    time.sleep(3.5)  # Pauză între linii

ser.close()


