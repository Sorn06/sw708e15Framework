#include "ostypes.h"
#include "types.h"




extern int16 n_HVMFramework_SPI_nInit(int32 *sp);
extern int16 n_HVMFramework_SPI_nTransfer(int32 *sp);

int16 n_HVMFramework_SPI_nInit(int32 *sp)
{
    //Parameters
    uint8_t enableInterrupt = sp[0];
    int8_t dataOrder = sp[1];
    uint8_t master = sp[2];
    int8_t divider = sp[3]; 
    int8_t polarity = sp[4];
    int8_t phase = sp[5];
    uint8_t doubleClockRate = sp[6];

    init(enableInterrupt,dataOrder,master,divider,polarity,phase,doubleClockRate)

    return -1;
}


int16 n_HVMFramework_SPI_Transfer(int32 *sp)
{
    int16_t val = sp[1];
    sp[0] = transfer(val);
    return -1;
}

