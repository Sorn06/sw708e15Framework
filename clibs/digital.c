#include "types.h"
#include "digitalWriteFast.h"

extern int16 n_lib_Digital_Write(int32 *sp);
extern int16 n_lib_Digital_PinMode(int32 *sp);
extern int16 n_lib_Digital_read(int32 *sp);

int16 n_HVMFramework_Digital_Write(int32 *sp)
{
  const int pin = sp[0];
  const int out = sp[1];
  digitalWriteFast(pin, out);
  return -1; // -1 if success, 0 if failure
}


int16 n_HVMFramework_Digital_read(int32 *sp) {
  Object* dat = HEAP_REF((Object*)(pointer)sp[0], Object*);
  dat[3] = digitalReadFast(dat[2]);
  return -1;
}

int16 n_HVMFramework_Digital_PinMode(int32 *sp)
{
  const int pin = sp[0];
  const int out = sp[1];
  pinModeFast(pin,out);
  return -1;
}
