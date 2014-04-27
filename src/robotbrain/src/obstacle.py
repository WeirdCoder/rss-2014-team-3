import location

class Obstacle(object):

	def __init__(self, locationList):
		self.locationList = locationList
		
		# calculating minX, maxX, minY, maxY
		xList = [point.getX() for point in locationList]
		yList = [point.getY() for point in locationList]

		if len(xList) > 0:
			self.minX = min(xList)
			self.maxX = max(xList)
			self.minY = min(yList)
			self.maxY = max(yList)
		else:
			self.minX = 0
			self.maxX = 0
			self.minY = 0
			self.maxY = 0

	def addXY(self, x, y):
		self.locationList.append(location.Location(x,y))
		
		#updating mins and maxes
		if x < self.minX: self.minX = x
		elif x > self.maxX: self.maxX = x
		
		if y < self.minY: self.minY = y
		elif y > self.maxY: self.maxY = y

	def addPoint(self, location):
		self.locationList.append(location)

		#updating mins and maxes
		x = location.getX()
		y = location.getY()

		if x < self.minX: self.minX = x
		elif x > self.maxX: self.maxX = x
		
		if y < self.minY: self.minY = y
		elif y > self.maxY: self.maxY = y

	def getLocationList(self):
		return self.locationList

	def getMinX(self):
		return self.minX

	def getMaxX(self):
		return self.maxX

	def getMinY(self):
		return self.minY

	def getMaxY(self):
		return self.maxY
