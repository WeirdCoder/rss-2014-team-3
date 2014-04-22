import Location

class Obstacle(object):
	def __init__(self): 
		self.locationList = []

	def __init__(self, locationList):
		self.locationList = locationList

	def addPoint(self, x, y):
		self.locationList.append(Location(x,y))

	def addPoint(self, location):
		self.locationList.append(location)

	def getLocationList(self):
		return self.locationList
