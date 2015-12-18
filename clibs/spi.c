#include <avr/io.h>
#include <avr/interrupt.h>
#include "devices.h"
#include "ostypes.h"
#include "types.h"

#define SPI_CLOCK_MASK 0x03

void init(uint8_t enableInterrupt, int8_t dataOrder, uint8_t master, int8_t divider, int8_t polarity, int8_t phase, uint8_t doubleClockRate)
{
    //Initailize SPI ports
    DDR_SPI &= ~((1<<DD_MOSI)|(1<<DD_MISO)|(1<<DD_SS)|(1<<DD_SCK));
    // Define the following pins as output
    DDR_SPI |= ((1<<DD_MOSI)|(1<<DD_SS)|(1<<DD_SCK));

	SPCR = ((1<<SPE)|               // SPI Enable
            (enableInterrupt << SPIE)|              // SPI Interupt Enable
            (dataOrder << DORD)|              // Data Order (0:MSB first / 1:LSB first)
            (master << MSTR)|              // Master/Slave select   
            (polarity << CPOL)|              // Clock Polarity (0:SCK low / 1:SCK hi when idle)
            (phase << CPHA));             // Clock Phase (0:leading / 1:trailing edge sampling)
            

    //SPI divider
    SPCR = (SPCR & ~SPI_CLOCK_MASK) | (divider & SPI_CLOCK_MASK);
    //Double clock rate
    SPSR = (doubleClockRate << SPI2X);              // Double Clock Rate

}

int16 transfer(int16_t val)
{
    SPDR = val;
    while  (!(SPSR & (1<<SPIF)))  ;
    return SPDR;
}
