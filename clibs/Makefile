USBPORT=3
hvm=$(shell pwd)/../hvm
clibs=$(shell pwd)
mega:
	cd $(hvm) ; avr-gcc -c -g -o classes.o classes.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o icecapvm.o icecapvm.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o methodinterpreter.o methodinterpreter.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o methods.o methods.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o gc.o gc.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o print.o print.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o natives_allOS.o natives_allOS.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o allocation_point.o allocation_point.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o natives_avr.o natives_avr.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o natives_arduino.o natives_arduino.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; cp $(clibs)/spi.c $(clibs)/uart.h $(clibs)/analog.h $(clibs)/analog.c $(clibs)/digital.c $(clibs)/digitalWriteFast.h $(clibs)/devices.h $(hvm)
	cd $(hvm) ; avr-gcc -c -g -o digital.o digital.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o spi.o spi.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o analog.o analog.c -DF_CPU=16000000UL -mmcu=atmega2560 -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	avr-gcc -o main $(hvm)/classes.o $(hvm)/icecapvm.o $(hvm)/methodinterpreter.o $(hvm)/methods.o $(hvm)/gc.o $(hvm)/print.o $(hvm)/natives_allOS.o $(hvm)/allocation_point.o $(hvm)/natives_avr.o $(hvm)/natives_arduino.o $(hvm)/digital.o $(hvm)/analog.o $(hvm)/spi.o -mmcu=atmega2560
	avr-size -C --mcu=atmega2560 main
	avr-strip main
	avr-objcopy -O ihex -R .eeprom main main.hex
	avrdude -p atmega2560 -c wiring -P /dev/ttyACM$(USBPORT) -U flash:w:main.hex -D

uno:
	cd $(hvm) ; avr-gcc -c -g -o classes.o classes.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=9
	cd $(hvm) ; avr-gcc -c -g -o icecapvm.o icecapvm.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o methodinterpreter.o methodinterpreter.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o methods.o methods.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o gc.o gc.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o print.o print.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o natives_allOS.o natives_allOS.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o allocation_point.o allocation_point.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o natives_avr.o natives_avr.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; cp $(clibs)/digital.c $(clibs)/digitalWriteFast.h $(clibs)/devices.h $(hvm) ; avr-gcc -c -g -o digital.o digital.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; avr-gcc -c -g -o digital.o digital.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=100 -DJAVA_STACK_SIZE=96
	cd $(hvm) ; cp $(clibs)/analog.c $(clibs)/analog.h $(hvm) ; avr-gcc -c -g -o analog.o analog.c -DF_CPU=16000000UL -mmcu=atmega328p -Wall -pedantic -Os -DJAVA_HEAP_SIZE=8000 -DJAVA_STACK_SIZE=96
	avr-gcc -o main $(hvm)/classes.o $(hvm)/icecapvm.o $(hvm)/methodinterpreter.o $(hvm)/methods.o $(hvm)/gc.o $(hvm)/print.o $(hvm)/natives_allOS.o $(hvm)/allocation_point.o $(hvm)/natives_avr.o $(hvm)/digital.o $(hvm)/analog.o -mmcu=atmega328p
	avr-size -C --mcu=atmega328p main
	avr-strip main
	avr-objcopy -O ihex -R .eeprom main main.hex
	avrdude -V -F -c arduino -p atmega328p -P /dev/ttyACM$(USBPORT) -U flash:w:main.hex -D
