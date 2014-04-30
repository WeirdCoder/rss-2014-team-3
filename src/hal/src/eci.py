import usb.core
import struct
from stm32f1xx import *

VENDOR_ID=0xFFFF
PRODUCT_ID=0x19FB

VENDOR_READ = 0xC0
VENDOR_WRITE = 0x40

CTRL_SET_POINTER = 0x20
CTRL_WRITE_REG = 0x21
CTRL_READ_REG = 0x22
CTRL_SET_INPUT_REPORT = 0x23
CTRL_SET_OUTPUT_REPORT = 0x24
CTRL_SET_ENABLED_REPORTS = 0x25

class ECI:
	def __init__(self):
		self.dev=usb.core.find(idVendor=VENDOR_ID, idProduct=PRODUCT_ID)
		if self.dev is None:
			raise ValueError('Device not found')
		self.stm32_abstr_init()
		self.eci_abstr_init()

	def set_pointer(self,addr,length):
		out_msg=struct.pack('IH',addr,length)
		assert self.dev.ctrl_transfer(VENDOR_WRITE,CTRL_SET_POINTER,0,0,out_msg) == len(out_msg)

	def do_read(self,length):
		return self.dev.ctrl_transfer(VENDOR_READ,CTRL_READ_REG,0,0,length)

	def do_write(self,msg):
		assert self.dev.ctrl_transfer(VENDOR_WRITE,CTRL_WRITE_REG,0,0,msg) == len(msg)

	def read_register(self,address):
		self.set_pointer(address,4)
		return struct.unpack('I',self.do_read(4))[0]

	def write_register(self,address,value):
		self.set_pointer(address,4)
		self.do_write(struct.pack('I',value))

	def set_bits_register(self,address,bits,mask=0):
		value=self.read_register(address)
		self.do_write(struct.pack('I',(value & ~mask) | bits))

	def set_bits_variable(self,variable,bits,mask=0):
		return (variable & ~mask) | bits

	# End USB functions, begin STM32F10x register functions

	def stm32_abstr_init(self):
		pass

	def reset_all_periph(self):
		# maybe don't want to reset GPIOB due to USB_DISCONNECT
		bits = APB2_AFIO | \
			APB2_IOPA | \
			APB2_IOPB | \
			APB2_IOPC | \
			APB2_IOPD | \
			APB2_IOPE | \
			APB2_IOPF | \
			APB2_IOPG | \
			APB2_ADC1 | \
			APB2_ADC2 | \
			APB2_TIM1 | \
			APB2_SPI1 | \
			APB2_TIM8 | \
			APB2_USART1 | \
			APB2_ADC3

		self.set_bits_register(RCC_BASE+APB2RSTR,bits)

	def enable_apb2_clock(self,bits):
		self.set_bits_register(RCC_BASE+RCC_APB2ENR,bits)

	def enable_apb1_clock(self,bits):
		self.set_bits_register(RCC_BASE+RCC_APB1ENR,bits)

	def configure_gpio(self,base,pin,mode):
		if pin<8:
			reg=GPIO_CRL
			mask=GPIO_MODE_MASK << (pin*4)
			cnf=mode << (pin*4)
		else:
			reg=GPIO_CRH
			mask=GPIO_MODE_MASK << ((pin-8)*4)
			cnf=mode << ((pin-8)*4)
		self.set_bits_register(base+reg,cnf,mask)

	def gpio_set_bits(self,base,bits):
		self.set_bits_register(base+GPIO_BSRR,bits)

	def gpio_clear_bits(self,base,bits):
		self.set_bits_register(base+GPIO_BRR,bits)

	def gpio_read(self,base):
		return self.read_register(base+GPIO_IDR)

	def configure_adc(self,base,):
		self.set_bits_register(base+ADC_CR2,0,ADC_ADON) # disable ADC
		self.set_bits_register(base+GPIO_BRR,bits)
		self.set_bits_register(base+GPIO_BRR,bits)
		raise NotImplementedError
		# enable adc

	def configure_adc_channel(self,base,channel):
		raise NotImplementedError

	def configure_timer(self,base,period=10000,prescaler=72,clock_divisor=TIM_CKD_DIV1,counter_mode=TIM_COUNTER_MODE_UP,preload=TIM_PRELOAD_DISABLE):
		self.set_bits_register(base+TIM_CR1,counter_mode | clock_divisor | preload, TIM_CR1_DIR | TIM_CR1_CMS | TIM_CR1_CKD | TIM_CR1_ARPE)
		self.write_register(base+TIM_ARR,period)
		self.write_register(base+TIM_PSC,prescaler)
		self.write_register(base+TIM_EGR,TIM_PSCRELOADMODE_IMMEDIATE)

	def configure_timer_oc(self,base,channel,mode=TIM_OCMODE_TIMING,preload=TIM_OCPRELOAD_DISABLE,outputstate=TIM_OUTPUTSTATE_DISABLE,pulse=0x0000,polarity=TIM_OCPOLARITY_HIGH,noutputstate=TIM_OUTPUTNSTATE_DISABLE,npolarity=TIM_OCNPOLARITY_HIGH,idlestate=TIM_OCIDLESTATE_RESET,nidlestate=TIM_OCNIDLESTATE_RESET):
		if channel==1:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC1E)
			self.set_bits_register(base+TIM_CCMR1,mode | preload,TIM_CCMR1_OC1M | TIM_CCMR1_CC1S | TIM_CCMR1_OC1PE)
			ccer=self.read_register(base+TIM_CCER)
			ccer=self.set_bits_variable(ccer,polarity | outputstate,TIM_CCER_CC1P | TIM_CCER_CC1E)
			if base==TIM1_BASE:
				ccer=self.set_bits_variable(ccer,npolarity | noutputstate,TIM_CCER_CC1NP | TIM_CCER_CC1NE)
				self.set_bits_register(base+TIM_CR2,idlestate | nidlestate,TIM_CR2_OIS1 | TIM_CR2_OIS1N)
			self.write_register(base+TIM_CCR1,pulse)
			self.write_register(base+TIM_CCER,ccer)
		elif channel==2:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC2E)
			self.set_bits_register(base+TIM_CCMR1,(mode | preload)<<8,TIM_CCMR1_OC2M | TIM_CCMR1_CC2S | TIM_CCMR1_OC2PE)
			ccer=self.read_register(base+TIM_CCER)
			ccer=self.set_bits_variable(ccer,(polarity | outputstate)<<4,TIM_CCER_CC2P | TIM_CCER_CC2E)
			if base==TIM1_BASE:
				ccer=self.set_bits_variable(ccer,(npolarity | noutputstate)<<4,TIM_CCER_CC2NP | TIM_CCER_CC2NE)
				self.set_bits_register(base+TIM_CR2,(idlestate | nidlestate)<<2,TIM_CR2_OIS2 | TIM_CR2_OIS2N)
			self.write_register(base+TIM_CCR2,pulse)
			self.write_register(base+TIM_CCER,ccer)
		elif channel==3:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC3E)
			self.set_bits_register(base+TIM_CCMR2,mode | preload,TIM_CCMR2_OC3M | TIM_CCMR2_CC3S | TIM_CCMR2_OC3PE)
			ccer=self.read_register(base+TIM_CCER)
			ccer=self.set_bits_variable(ccer,(polarity | outputstate)<<8,TIM_CCER_CC3P | TIM_CCER_CC3E)
			if base==TIM1_BASE:
				ccer=self.set_bits_variable(ccer,(npolarity | noutputstate)<<8,TIM_CCER_CC3NP | TIM_CCER_CC3NE)
				self.set_bits_register(base+TIM_CR2,(idlestate | nidlestate)<<4,TIM_CR2_OIS3 | TIM_CR2_OIS3N)
			self.write_register(base+TIM_CCR3,pulse)
			self.write_register(base+TIM_CCER,ccer)
		elif channel==4:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC4E)
			self.set_bits_register(base+TIM_CCMR2,(mode | preload)<<8,TIM_CCMR2_OC4M | TIM_CCMR2_CC4S | TIM_CCMR2_OC4PE)
			ccer=self.read_register(base+TIM_CCER)
			ccer=self.set_bits_variable(ccer,(polarity | outputstate)<<12,TIM_CCER_CC4P | TIM_CCER_CC4E)
			if base==TIM1_BASE:
				ccer=self.set_bits_variable(ccer,(npolarity | noutputstate)<<12,TIM_CCER_CC4NP | TIM_CCER_CC4NE)
				self.set_bits_register(base+TIM_CR2,(idlestate | nidlestate)<<6,TIM_CR2_OIS4 | TIM_CR2_OIS4N)
			self.write_register(base+TIM_CCR4,pulse)
			self.write_register(base+TIM_CCER,ccer)
		else:
			raise Exception('Bad Channel')

	def set_timer_channel_pulse(self,base,channel,pulse):
		if channel==1:
			self.write_register(base+TIM_CCR1,pulse)
		elif channel==2:
			self.write_register(base+TIM_CCR2,pulse)
		elif channel==3:
			self.write_register(base+TIM_CCR3,pulse)
		elif channel==4:
			self.write_register(base+TIM_CCR4,pulse)
		else:
			raise Exception('Bad Channel')

	def read_timer_channel_pulse(self,base,channel):
		if channel==1:
			return self.read_register(base+TIM_CCR1)
		elif channel==2:
			return self.read_register(base+TIM_CCR2)
		elif channel==3:
			return self.read_register(base+TIM_CCR3)
		elif channel==4:
			return self.read_register(base+TIM_CCR4)
		else:
			raise Exception('Bad Channel')

	def configure_timer_ic(self,base,channel,selection=TIM_ICSELECTION_DIRECTTI,prescaler=TIM_ICPSC_DIV1,polarity=TIM_ICPOLARITY_RISING,filter=0x00,inputstate=TIM_INPUTSTATE_DISABLE):
		if channel==1:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC1E)
			self.set_bits_register(base+TIM_CCMR1,selection | (filter<<4) | prescaler,TIM_CCMR1_CC1S | TIM_CCMR1_IC1F | TIM_CCMR1_IC1PSC)
			self.set_bits_register(base+TIM_CCER,polarity | inputstate,TIM_CCER_CC1P)
		elif channel==2:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC2E)
			self.set_bits_register(base+TIM_CCMR1,(selection | (filter<<4) | prescaler)<<8,TIM_CCMR1_CC2S | TIM_CCMR1_IC2F | TIM_CCMR1_IC2PSC)
			self.set_bits_register(base+TIM_CCER,(polarity | inputstate)<<4,TIM_CCER_CC2P)
		elif channel==3:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC3E)
			self.set_bits_register(base+TIM_CCMR2,selection | (filter<<4) | prescaler,TIM_CCMR2_CC3S | TIM_CCMR2_IC3F | TIM_CCMR2_IC3PSC)
			self.set_bits_register(base+TIM_CCER,(polarity | inputstate)<<8,TIM_CCER_CC3P)
		elif channel==4:
			self.set_bits_register(base+TIM_CCER,0,TIM_CCER_CC4E)
			self.set_bits_register(base+TIM_CCMR2,(selection | (filter<<4) | prescaler)<<8,TIM_CCMR2_CC4S | TIM_CCMR2_IC4F | TIM_CCMR2_IC4PSC)
			self.set_bits_register(base+TIM_CCER,(polarity | inputstate)<<12,TIM_CCER_CC4P)
		else:
			raise Exception('Bad Channel')

	def configure_timer_slave(self,base,mode=TIM_MASTERSLAVEMODE_DISABLE,selection=TIM_SLAVEMODE_RESET,trigger=TIM_TS_ITR0):
		self.set_bits_register(base+TIM_SMCR,mode | selection | trigger, TIM_SMCR_MSM | TIM_SMCR_TS | TIM_SMCR_SMS)

	def enable_timer(self,base):
		self.set_bits_register(base+TIM_CR1,TIM_CR1_CEN)
		if base == TIM1_BASE:
			self.set_bits_register(base+TIM_BDTR,TIM_BDTR_MOE)

	# End USB functions, begin ECI abstraction

	GPIO_MAP = {
		'D0':(GPIO_A_BASE,8),
		'D1':(GPIO_A_BASE,9),
		'D2':(GPIO_A_BASE,10),
		'D3':(GPIO_B_BASE,3),
		'D4':(GPIO_B_BASE,4),
		'D5':(GPIO_B_BASE,5),
		'D6':(GPIO_B_BASE,6),
		'D7':(GPIO_B_BASE,7),
		'D8':(GPIO_B_BASE,8),
		'D9':(GPIO_B_BASE,9),
		'A0':(GPIO_A_BASE,0),
		'A1':(GPIO_A_BASE,1),
		'A2':(GPIO_A_BASE,2),
		'A3':(GPIO_A_BASE,3),
		'A4':(GPIO_A_BASE,4),
		'A5':(GPIO_A_BASE,5),
		'A6':(GPIO_A_BASE,6),
		'A7':(GPIO_A_BASE,7),
		'A8':(GPIO_B_BASE,0),
		'A9':(GPIO_B_BASE,1),
		'E0':(GPIO_B_BASE,10),
		'E1':(GPIO_B_BASE,11),
		'E2':(GPIO_B_BASE,12),
		'E3':(GPIO_B_BASE,13),
		'E4':(GPIO_B_BASE,14),
		'E6':(GPIO_A_BASE,15),
	}

	GPIO_OUT_MODES={
		'pp':GPIO_MODE_OUTPUT_PP,
		'od':GPIO_MODE_OUTPUT_OD
	}

	GPIO_AF_MODES={
		'pp':GPIO_MODE_OUTPUT_AF_PP,
		'od':GPIO_MODE_OUTPUT_AF_OD
	}

	GPIO_IN_MODES={
		'floating':GPIO_MODE_INPUT_FLOATING,
		'pupd':GPIO_MODE_INPUT_PU_PD,
	}

	ADC_MAP={
		'A0',(ADC1_BASE,0),
		'A1',(ADC1_BASE,1),
		'A2',(ADC1_BASE,2),
		'A3',(ADC1_BASE,3),
		'A4',(ADC1_BASE,4),
		'A5',(ADC1_BASE,5),
		'A6',(ADC1_BASE,6),
		'A7',(ADC1_BASE,7),
		'A8',(ADC1_BASE,8),
		'A9',(ADC1_BASE,9),
	}

	TIM_MAP={
		'D0':(TIM1_BASE,1),
		'D1':(TIM1_BASE,2),
		'D2':(TIM1_BASE,3),
		'D3':(TIM2_BASE,2),
		'D4':(TIM3_BASE,1),
		'D5':(TIM3_BASE,2),
		'D6':(TIM4_BASE,1),
		'D7':(TIM4_BASE,2),
		'D8':(TIM4_BASE,3),
		'D9':(TIM4_BASE,4),
	}

	def eci_abstr_init(self):
		self.functions={
			'D0':'',
			'D1':'',
			'D2':'',
			'D3':'',
			'D4':'',
			'D5':'',
			'D6':'',
			'D7':'',
			'D8':'',
			'D9':'',
			'A0':'',
			'A1':'',
			'A2':'',
			'A3':'',
			'A4':'',
			'A5':'',
			'A6':'',
			'A7':'',
			'A8':'',
			'A9':'',
			'E0':'',
			'E1':'',
			'E2':'',
			'E3':'',
			'E4':'',
			'E5':'',
		}

		bits = APB2_IOPA | \
		       APB2_IOPB | \
		       APB2_ADC1 | \
		       APB2_TIM1 | \
		       APB2_AFIO
		self.enable_apb2_clock(bits)

		bits = APB1_TIM2 | \
		       APB1_TIM3 | \
		       APB1_TIM4
		self.enable_apb1_clock(bits)

		self.configure_timer(TIM1_BASE)
		self.configure_timer(TIM2_BASE)
		self.configure_timer(TIM3_BASE)
		self.configure_timer(TIM4_BASE)

		self.enable_timer(TIM1_BASE)
		self.enable_timer(TIM2_BASE)
		self.enable_timer(TIM3_BASE)
		self.enable_timer(TIM4_BASE)

		self.set_bits_register(AFIO_BASE+AFIO_MAPR,0x02000900,0x07000F00)

	def set_out(self,pin,mode='pp',value=0):
		assert pin in self.GPIO_MAP
		(gpio,p)=self.GPIO_MAP[pin]
		if value:
			self.gpio_set_bits(gpio,1<<p)
		else:
			self.gpio_clear_bits(gpio,1<<p)	
		cnf=self.GPIO_OUT_MODES[mode]
		self.configure_gpio(gpio,p,cnf)
		self.functions[pin]='out'

	def set_in(self,pin,mode='floating',pull_direction=0):
		assert pin in self.GPIO_MAP
		(gpio,p)=self.GPIO_MAP[pin]
		cnf=self.GPIO_IN_MODES[mode]
		self.configure_gpio(gpio,p,cnf)
		if mode=='pupd':
			if pull_direction:
				self.gpio_set_bits(gpio,1<<p)
			else:
				self.gpio_clear_bits(gpio,1<<p)	
		self.functions[pin]='in'

	def set_adc(self,pin):
		assert pin in self.ADC_MAP
		(gpio,p)=self.GPIO_MAP[pin]
		cnf=GPIO_MODE_INPUT_ANALOG
		self.configure_gpio(gpio,p,cnf)
		self.functions[pin]='adc'

	def set_pwm_out(self,pin,mode='pp'):
		assert pin in self.TIM_MAP
		# Set up PWM
		(timer,channel)=self.TIM_MAP[pin]
		self.configure_timer_oc(timer,channel,mode=TIM_OCMODE_PWM1,outputstate=TIM_OUTPUTSTATE_ENABLE,preload=TIM_OCPRELOAD_ENABLE)
		# Set up GPIO
		(gpio,p)=self.GPIO_MAP[pin]
		cnf=self.GPIO_AF_MODES[mode]
		self.configure_gpio(gpio,p,cnf)
		self.functions[pin]='pwm_out'

	def set_pwm_in(self,pin,mode='floating'):
		assert pin in self.TIM_MAP
		# Set up PWM
		(timer,channel)=self.TIM_MAP[pin]
		self.configure_timer_ic(timer,channel,inputstate=TIM_INPUTSTATE_ENABLE,polarity=TIM_ICPOLARITY_FALLING)
		# Set up GPIO
		(gpio,p)=self.GPIO_MAP[pin]
		cnf=self.GPIO_IN_MODES[mode]
		self.configure_gpio(gpio,p,cnf)
		self.functions[pin]='pwm_in'

	def out(self,pin,val):
		assert pin in self.GPIO_MAP
		if self.functions[pin] != 'out':
			self.set_out(pin)
		(gpio,p)=self.GPIO_MAP[pin]
		if val:
			self.gpio_set_bits(gpio,1<<p)
		else:
			self.gpio_clear_bits(gpio,1<<p)

	def pwm_out(self,pin,width):
		assert pin in self.TIM_MAP
		if self.functions[pin] != 'pwm_out':
			self.set_pwm_out(pin)
		(timer,channel)=self.TIM_MAP[pin]
		self.set_timer_channel_pulse(timer,channel,width)

	def pwm_in(self,pin):
		assert pin in self.TIM_MAP
		if self.functions[pin] != 'pwm_in':
			self.set_pwm_in(pin)
		(timer,channel)=self.TIM_MAP[pin]
		return self.read_timer_channel_pulse(timer,channel)

	def d_in(self,pin):
		assert pin in self.GPIO_MAP
		if self.functions[pin] != 'in':
			self.set_in(pin)
		(gpio,p)=self.GPIO_MAP[pin]
		return bool(self.gpio_read(gpio) & (1<<p))

	def a_in(self,pin):
		assert pin in self.ADC_MAP
		if self.functions[pin] != 'adc':
			self.set_adc(pin)

if __name__=='__main__':


	import time
	import math
	e=ECI()

	def read_sonar(sonar):
		if sonar==1:
			e.configure_timer_slave(TIM1_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI1FP1)
			e.set_bits_register(TIM1_BASE+TIM_SR,0,TIM_SR_CC1IF)
			e.out('D0',1)
			e.pwm_in('D0')
			while not (e.read_register(TIM1_BASE+TIM_SR) & TIM_SR_CC1IF):
				time.sleep(0.01)
			return e.pwm_in('D0')-750
		elif sonar==2:
			e.configure_timer_slave(TIM1_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI2FP2)
			e.set_bits_register(TIM1_BASE+TIM_SR,0,TIM_SR_CC2IF)
			e.out('D1',1)
			e.pwm_in('D1')
			while not (e.read_register(TIM1_BASE+TIM_SR) & TIM_SR_CC2IF):
				time.sleep(0.01)
			return e.pwm_in('D1')-750
		elif sonar==3:
			e.configure_timer_slave(TIM3_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI1FP1)
			e.set_bits_register(TIM3_BASE+TIM_SR,0,TIM_SR_CC1IF)
			e.out('D4',1)
			e.pwm_in('D4')
			while not (e.read_register(TIM3_BASE+TIM_SR) & TIM_SR_CC1IF):
				time.sleep(0.01)
			return e.pwm_in('D4')-750
		elif sonar==4:
			e.configure_timer_slave(TIM3_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI2FP2)
			e.set_bits_register(TIM3_BASE+TIM_SR,0,TIM_SR_CC2IF)
			e.out('D5',1)
			e.pwm_in('D5')
			while not (e.read_register(TIM3_BASE+TIM_SR) & TIM_SR_CC2IF):
				time.sleep(0.01)
			return e.pwm_in('D5')-750
		else:
			raise Exception('Bad sonar')

	e.configure_timer(TIM1_BASE,period=30000,prescaler=72) # SONAR
	e.configure_timer(TIM2_BASE,period=10000,prescaler=72,preload=TIM_PRELOAD_ENABLE) # PWM
	e.configure_timer(TIM3_BASE,period=30000,prescaler=72,preload=TIM_PRELOAD_ENABLE) # SONAR
	e.configure_timer(TIM4_BASE,period=10000,prescaler=72,preload=TIM_PRELOAD_ENABLE) # PWM

	e.pwm_out('D3',1500)
	e.pwm_out('D6',1500)
	e.pwm_out('D7',1500)
	e.pwm_out('D8',1500)
	e.pwm_out('D9',1500)

	e.set_in('A0','pupd',1)
	e.set_in('A1','pupd',1)

	while True:
		#print read_sonar(1)
		#print read_sonar(2)
		#print read_sonar(4)
		#print read_sonar(4)
		print e.d_in('A0')
		#print e.d_in('A1')
		time.sleep(0.05)

	#while True:
	#	e.out('D0',1)
	#	print e.d_in('D1')
	#	time.sleep(1)
	#	e.out('D0',0)
	#	print e.d_in('D1')
	#	time.sleep(1)

