// License Agreement
//
// Copyright (c) 2011, 2012, 2013, 2014, 2015 Watterott electronic (www.watterott.com)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice, this 
//   list of conditions and the following disclaimer.
//
// * Redistributions in binary form must reproduce the above copyright notice, this
//   list of conditions and the following disclaimer in the documentation and/or
//   other materials provided with the distribution.
//
// * Neither the name of the copyright holders nor the names of its contributors
//   may be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
// SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
// IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

#ifndef __devices_h_
#define __devices_h_ 1






//Arduino Mega
#if (defined(ARDUINO_AVR_MEGA) || \
    defined(ARDUINO_AVR_MEGA1280) || \
    defined(ARDUINO_AVR_MEGA2560) || \
    defined(__AVR_ATmega1280__) || \
    defined(__AVR_ATmega1281__) || \
    defined(__AVR_ATmega2560__) || \
    defined(__AVR_ATmega2561__))


#define PORT_SPI    PORTB
#define DDR_SPI     DDRB
#define DD_MISO     PORTB3 
#define DD_MOSI     PORTB2
#define DD_SS       PORTB0
#define DD_SCK      PORTB1

#define __digitalPinToPortReg(P) \
  (((P) >= 22 && (P) <= 29) ? &PORTA : \
   ((((P) >= 10 && (P) <= 13) || ((P) >= 50 && (P) <= 53)) ? &PORTB : \
    (((P) >= 30 && (P) <= 37) ? &PORTC : \
     ((((P) >= 18 && (P) <= 21) || (P) == 38) ? &PORTD : \
      ((((P) >= 0 && (P) <= 3) || (P) == 5) ? &PORTE : \
       (((P) >= 54 && (P) <= 61) ? &PORTF : \
        ((((P) >= 39 && (P) <= 41) || (P) == 4) ? &PORTG : \
         ((((P) >= 6 && (P) <= 9) || (P) == 16 || (P) == 17) ? &PORTH : \
          (((P) == 14 || (P) == 15) ? &PORTJ : \
           (((P) >= 62 && (P) <= 69) ? &PORTK : &PORTL))))))))))

#define __digitalPinToDDRReg(P) \
  (((P) >= 22 && (P) <= 29) ? &DDRA : \
   ((((P) >= 10 && (P) <= 13) || ((P) >= 50 && (P) <= 53)) ? &DDRB : \
    (((P) >= 30 && (P) <= 37) ? &DDRC : \
     ((((P) >= 18 && (P) <= 21) || (P) == 38) ? &DDRD : \
      ((((P) >= 0 && (P) <= 3) || (P) == 5) ? &DDRE : \
       (((P) >= 54 && (P) <= 61) ? &DDRF : \
        ((((P) >= 39 && (P) <= 41) || (P) == 4) ? &DDRG : \
         ((((P) >= 6 && (P) <= 9) || (P) == 16 || (P) == 17) ? &DDRH : \
          (((P) == 14 || (P) == 15) ? &DDRJ : \
           (((P) >= 62 && (P) <= 69) ? &DDRK : &DDRL))))))))))

#define __digitalPinToPINReg(P) \
  (((P) >= 22 && (P) <= 29) ? &PINA : \
   ((((P) >= 10 && (P) <= 13) || ((P) >= 50 && (P) <= 53)) ? &PINB : \
    (((P) >= 30 && (P) <= 37) ? &PINC : \
     ((((P) >= 18 && (P) <= 21) || (P) == 38) ? &PIND : \
      ((((P) >= 0 && (P) <= 3) || (P) == 5) ? &PINE : \
       (((P) >= 54 && (P) <= 61) ? &PINF : \
        ((((P) >= 39 && (P) <= 41) || (P) == 4) ? &PING : \
         ((((P) >= 6 && (P) <= 9) || (P) == 16 || (P) == 17) ? &PINH : \
          (((P) == 14 || (P) == 15) ? &PINJ : \
           (((P) >= 62 && (P) <= 69) ? &PINK : &PINL))))))))))

#define __digitalPinToBit(P) \
  (((P) >=  7 && (P) <=  9) ? (P) - 3 : \
   (((P) >= 10 && (P) <= 13) ? (P) - 6 : \
    (((P) >= 22 && (P) <= 29) ? (P) - 22 : \
     (((P) >= 30 && (P) <= 37) ? 37 - (P) : \
      (((P) >= 39 && (P) <= 41) ? 41 - (P) : \
       (((P) >= 42 && (P) <= 49) ? 49 - (P) : \
        (((P) >= 50 && (P) <= 53) ? 53 - (P) : \
         (((P) >= 54 && (P) <= 61) ? (P) - 54 : \
          (((P) >= 62 && (P) <= 69) ? (P) - 62 : \
           (((P) == 0 || (P) == 15 || (P) == 17 || (P) == 21) ? 0 : \
            (((P) == 1 || (P) == 14 || (P) == 16 || (P) == 20) ? 1 : \
             (((P) == 19) ? 2 : \
              (((P) == 5 || (P) == 6 || (P) == 18) ? 3 : \
               (((P) == 2) ? 4 : \
                (((P) == 3 || (P) == 4) ? 5 : 7)))))))))))))))


//Arduino Uno
#elif (defined(ARDUINO_AVR_UNO) || \
    defined(ARDUINO_AVR_DUEMILANOVE) || \
    defined(ARDUINO_ARCH_AVR) || \
    defined(__AVR_ATmega328__) || \
    defined(__AVR_ATmega328P__) || \
    defined(__AVR__))

#define PORT_SPI    PORTB
#define DDR_SPI     DDRB
#define DD_MISO     PORTB4
#define DD_MOSI     PORTB3
#define DD_SS       PORTB2
#define DD_SCK      PORTB5

#define __digitalPinToPortReg(P) \
  (((P) >= 0 && (P) <= 7) ? &PORTD : (((P) >= 8 && (P) <= 13) ? &PORTB : &PORTC))
#define __digitalPinToDDRReg(P) \
  (((P) >= 0 && (P) <= 7) ? &DDRD : (((P) >= 8 && (P) <= 13) ? &DDRB : &DDRC))
#define __digitalPinToPINReg(P) \
  (((P) >= 0 && (P) <= 7) ? &PIND : (((P) >= 8 && (P) <= 13) ? &PINB : &PINC))
#define __digitalPinToBit(P) \
  (((P) >= 0 && (P) <= 7) ? (P) : (((P) >= 8 && (P) <= 13) ? (P) - 8 : (P) - 14))

#endif

#endif
