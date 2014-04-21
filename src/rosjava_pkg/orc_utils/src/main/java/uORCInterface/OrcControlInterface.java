package uORCInterface;

public abstract interface OrcControlInterface
{
  public abstract void motorSlewWrite(int paramInt1, int paramInt2);
  
  public abstract void motorSet(int paramInt1, int paramInt2);
  
  public abstract void servoWrite(int paramInt1, int paramInt2);
  
  public abstract long clockReadSlave();
  
  public abstract int readEncoder(int paramInt);
  
  public abstract int readVelocity(int paramInt);
  
  public abstract double analogRead(int paramInt);
  
  public abstract void digitalSet(int paramInt, boolean paramBoolean);
  
  public abstract boolean digitalRead(int paramInt);
}


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\uORCInterface-0.0.jar
 * Qualified Name:     uORCInterface.OrcControlInterface
 * JD-Core Version:    0.7.0.1
 */