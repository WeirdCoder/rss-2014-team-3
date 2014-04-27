import location

class Obstacle(object):

	def __init__(self, locationList):
		self.locationList = locationList
		
		# calculating minX, maxX, minY, maxY
		xList = [point.getX() for point in locationList]
		yList = [point.getY() for point in locationList]
		self.minX = min(xList)
		self.maxX = max(xList)
		self.minY = min(yList)
		self.maxY = max(yList)

	def addPoint(self, x, y):
		self.locationList.append(location.Location(x,y))

	def addPoint(self, location):
		self.locationList.append(location)

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
