// Author: Mosam
// LCD is used for system actuation based on Pitch calculated using Android Smartphone
// And the Pitch of the sound was calculated and correspondingly the LCD System was made consisting of Bar Graph Representation in 1st Row and "Pitch Analyser" written in 2nd Row. Thus, corresponding to different values of Pitch of the sound, the Bar Graph changed accordingly.

#include <Arduino.h>
#include <SoftwareSerial.h>
#include <LiquidCrystal.h>
#include <LcdBarGraph.h>
#include "WiFly.h"
 
SoftwareSerial uart(2, 3); 
LiquidCrystal lcd(12, 11, 5, 4, 7, 6);
WiFly wifly(&uart); 
byte lcdNumCols = 16; 
//byte sensorPin = 0; 
LcdBarGraph lbg(&lcd, lcdNumCols); 



  void setup() 
  {
  uart.begin(9600); 
  Serial.begin(9600); // start the Arduino serial monitor window connection
  delay(3000); // wait 3 second to allow the serial/uart object to start
  lcd.begin(lcdNumCols,2);
  lcd.clear();
//  delay(100);
  }
 
  void loop() 
  {

    char buffer[] = {' ',' ',' ',' ',' ',' ',' '};
  while (!wifly.available());  // if there is data available from the shield
    {
          
     wifly.readBytesUntil('n', buffer, 3);
     int incomingValue = atoi(buffer);
     //int incomming = wifly.read();
     //incomming = incomming - 48;
    int incoming = constrain(incomingValue,100,600);
    incoming = map(incoming,100,600,0,1023);
     Serial.println(incomingValue);
      lcd.clear();
            lcd.setCursor(1,1);
    lcd.print("Pitch Analyser");

       lcd.setCursor(0, 0);
       lbg.drawValue((byte) incoming,250);
       delay(35);
      
     
    }
 
  while (Serial.available()) // if we typed a command
    {
    wifly.write(Serial.read()); // send the command to the WiFi shield.
    }
  }
  
  
 
