//
// begin license header
//
// This file is part of Pixy CMUcam5 or "Pixy" for short
//
// All Pixy source code is provided under the terms of the
// GNU General Public License v2 (http://www.gnu.org/licenses/gpl-2.0.html).
// Those wishing to use Pixy source code, software and/or
// technologies under different licensing terms should contact us at
// cmucam@cs.cmu.edu. Such licensing terms are available for
// all portions of the Pixy codebase presented here.
//
// end license header
//
// This sketch is a good place to start if you're just getting started with 
// Pixy and Arduino.  This program simply prints the detected object blocks 
// (including color codes) through the serial console.  It uses the Arduino's 
// ICSP port.  For more information go here:
//
// http://cmucam.org/projects/cmucam5/wiki/Hooking_up_Pixy_to_a_Microcontroller_(like_an_Arduino)
//
// It prints the detected blocks once per second because printing all of the 
// blocks for all 50 frames per second would overwhelm the Arduino's serial port.
//
   
#include <SPI.h>  
#include <Pixy.h>
#include <Wire.h>


// This is the main Pixy object 
Pixy pixy;

bool exists = false;
int x;
int y;

void requestEvent();

void setup()
{
  Serial.begin(9600);
  Serial.print("Starting...\n");
  Wire.begin(4);
  Wire.onRequest(requestEvent);
  pixy.init();
}

void requestEvent() {
   byte yValue = (byte) y;
   byte rightX = (byte) x;
   byte leftX = (byte) (x >> 8);
   Serial.println(leftX);
   Wire.write(yValue);
   Wire.write(rightX);
   Wire.write(leftX);
  
}

void loop()
{ 
  static int i = 0;
  int j;
  uint16_t blocks;
  char buf[32]; 
  // grab blocks!
 // Serial.println(exists);
  // If there are detect blocks, print them!
        blocks = pixy.getBlocks();
    i++;
    
    // do this (print) every 50 frames because printing every
    // frame would bog down the Arduino
    if (i%50==0)
    {

        x = pixy.blocks[0].x;
        y = pixy.blocks[0].y;
      
    }
  
}

