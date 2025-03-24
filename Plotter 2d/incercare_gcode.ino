#define X_STEP_PIN 54
#define X_DIR_PIN 55
#define X_ENABLE_PIN 38

#define Y_STEP_PIN 60
#define Y_DIR_PIN 61
#define Y_ENABLE_PIN 56

#define Z_STEP_PIN 46
#define Z_DIR_PIN 48
#define Z_ENABLE_PIN 62

#define STEPS_PER_MM 80  // Pași/mm pentru X și Y

; // Întârziere între pași în microsecunde

float current_x = 0;
float current_y = 0;
float home_x = 0;
float home_y = 0;
bool pen_down = false;
float feed_rate = 1000;  // Viteza în mm/min
float STEP_DELAY = max(800, 60000000 / (feed_rate * STEPS_PER_MM));

void setup() {
  pinMode(X_STEP_PIN, OUTPUT);
  pinMode(X_DIR_PIN, OUTPUT);
  pinMode(X_ENABLE_PIN, OUTPUT);

  pinMode(Y_STEP_PIN, OUTPUT);
  pinMode(Y_DIR_PIN, OUTPUT);
  pinMode(Y_ENABLE_PIN, OUTPUT);

  pinMode(Z_STEP_PIN, OUTPUT);
  pinMode(Z_DIR_PIN, OUTPUT);
  pinMode(Z_ENABLE_PIN, OUTPUT);

  digitalWrite(X_ENABLE_PIN, LOW);
  digitalWrite(Y_ENABLE_PIN, LOW);
  digitalWrite(Z_ENABLE_PIN, LOW);

  Serial.begin(57600);
  Serial.println("G-code Plotter Ready!");
  Serial.println("Send command 'SET_HOME' to save this position as home.");
}

void loop() {
  if (Serial.available()) {
    String input = Serial.readStringUntil('\n');
    input.trim();

    if (input == "SET_HOME") {
      home_x = current_x;
      home_y = current_y;
      Serial.println("Home position set successfully.");
    } 
    else if (input == "G21") {
      Serial.println("Unit set to millimeters (G21).");
    } 
    else if (input == "G90") {
      Serial.println("Set to absolute positioning (G90).");
    } 
    else if (input == "M3") {
      Serial.println("Pen activated.");
      pen_down = true;
    } 
    else if (input == "M5") {
      Serial.println("Pen deactivated.");
      pen_down = false;
    } 
    else if (input == "GOTO_HOME") {
      Serial.println("Moving to home position...");
      moveTo(home_x, home_y);
      current_x = home_x;
      current_y = home_y;
    }
    else {
      processCommand(input);
    }
  }
}

void processCommand(String command) {
  if (command.startsWith("G0") || command.startsWith("G1")) {
    float target_x = current_x;
    float target_y = current_y;

    if (command.indexOf('X') != -1) {
      target_x = command.substring(command.indexOf('X') + 1).toFloat();
    }
    if (command.indexOf('Y') != -1) {
      target_y = command.substring(command.indexOf('Y') + 1).toFloat();
    }
    if (command.indexOf('F') != -1) {
      feed_rate = command.substring(command.indexOf('F') + 1).toFloat();
      STEP_DELAY = 60000000 / (feed_rate * STEPS_PER_MM);
      Serial.print("Feed rate updated to: ");
      Serial.println(feed_rate);
    }

    moveTo(target_x, target_y);
    current_x = target_x;
    current_y = target_y;
  }
}

void moveTo(float target_x, float target_y) {
    int steps_x = (target_x - current_x) * STEPS_PER_MM;
    int steps_y = (target_y - current_y) * STEPS_PER_MM;

    bool dir_x = steps_x > 0;
    bool dir_y = steps_y > 0;

    steps_x = abs(steps_x);
    steps_y = abs(steps_y);

    digitalWrite(X_DIR_PIN, dir_x ? HIGH : LOW);
    digitalWrite(Y_DIR_PIN, dir_y ? HIGH : LOW);

    int dx = steps_x;
    int dy = steps_y;
    int sx = (dx > 0) ? 1 : -1;
    int sy = (dy > 0) ? 1 : -1;
    int err = dx - dy;

    while (dx > 0 || dy > 0) {
        if (2 * err >= -dy) {
            if (dx > 0) {
                digitalWrite(X_STEP_PIN, HIGH);
                delayMicroseconds(STEP_DELAY / 2);
                digitalWrite(X_STEP_PIN, LOW);
                delayMicroseconds(STEP_DELAY / 2);
                dx--;
            }
            err -= dy;
        }
        if (2 * err <= dx) {
            if (dy > 0) {
                digitalWrite(Y_STEP_PIN, HIGH);
                delayMicroseconds(STEP_DELAY / 2);
                digitalWrite(Y_STEP_PIN, LOW);
                delayMicroseconds(STEP_DELAY / 2);
                dy--;
            }
            err += dx;
        }
    }

    Serial.print("Moved to: X=");
    Serial.print(target_x);
    Serial.print(", Y=");
    Serial.println(target_y);
}




