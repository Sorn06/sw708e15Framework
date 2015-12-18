#include "analog.h"
#include "types.h"

extern int16 n_lib_Analog_Init(int32 *sp);
extern int16 n_lib_Analog_read(int32 *sp);

int16 n_HVMFramework_Analog_read(int32 *sp)
{
  Object* dat = HEAP_REF((Object*)(pointer)sp[0], Object*);
  uint16 valid = (dat[2] >= 0 && dat[2] <= 15) ? -1 : 0;
  if(valid == -1)
      dat[3] = adc_read(dat[2]);
  return -1;
}

int16 n_HVMFramework_Analog_Init(int32 *sp)
{
    adc_init();
    return -1;
}
