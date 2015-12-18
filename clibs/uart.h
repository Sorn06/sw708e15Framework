#define BAUDRATE 9600      //
#define F_CPU 16000000UL
#define BAUD_PRESCALLER (((F_CPU / (BAUDRATE * 16UL))) - 1)    //The formula that does all the required maths

#include <avr/io.h>            //General definitions of the registers values
#include <util/delay.h>            //This is where the delay functions are located
 
void USART_init(void){
 
 UBRR0H = (uint8_t)(BAUD_PRESCALLER>>8);
 UBRR0L = (uint8_t)(BAUD_PRESCALLER);
 UCSR0B = (1<<RXEN0)|(1<<TXEN0);
 UCSR0C = ((1<<UCSZ00)|(1<<UCSZ01));
}
void USART_send( unsigned char data){
 while(!(UCSR0A & (1<<UDRE0)));
 UDR0 = data;
 
}
unsigned char USART_receive(void){
 
 while(!(UCSR0A & (1<<RXC0)));
 return UDR0;
 
}
