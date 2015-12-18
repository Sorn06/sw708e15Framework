#include <avr/io.h>            //General definitions of the registers values
void adc_init(void)
{
    /*Set the division factor according to the CPU clock speed*/
    #if F_CPU >= 16000000 
    ADCSRA |= ((1<<ADPS2) |(1<<ADPS1) |(1<<ADPS0));
    #elif F_CPU >= 8000000 
        ADCSRA |= ((1<<ADPS2) |(1<<ADPS1) |(0<<ADPS0));
    #elif F_CPU >= 4000000 
        ADCSRA |= ((1<<ADPS2) |(0<<ADPS1) |(1<<ADPS0));
    #elif F_CPU >= 2000000 
        ADCSRA |= ((1<<ADPS2) |(0<<ADPS1) |(0<<ADPS0));
    #elif F_CPU >= 1000000 
        ADCSRA |= ((0<<ADPS2) |(1<<ADPS1) |(1<<ADPS0));
    #endif
    /*
    * We require an external capacitor.
    */
    ADMUX |= (1<<REFS0);
    /*Enable the ADC*/
    ADCSRA |= (1<<ADEN);
    /*
    * The first the conversion after enabling the ADC takes 25 ADC clock cycles.
    * Normal conversions takes now only 13 ADC clock cycles.
     */
    ADCSRA |= (1<<ADSC);
}
uint16_t adc_read(uint8_t pin)
{
    /*Clear MUX bits.*/
    ADMUX &=~ ((1 << MUX0)|(1 << MUX1)|(1 << MUX2)|(1 << MUX3));
    /*Set the MUX5 bit if pin is above 8*/
    if(pin >= 8){
        ADCSRB |= (1 << MUX5);
    }
    else{
        ADCSRB &=~ (1 << MUX5);
    }
    /*Set the ADMUX bits MUX0, MUX1 and MUX2 to match the input pin*/
    ADMUX |= pin&0x07;
    /*Start the analog conversion*/
    ADCSRA |= (1<<ADSC);
    while(ADCSRA & (1<<ADSC));
    return ADC;
}
