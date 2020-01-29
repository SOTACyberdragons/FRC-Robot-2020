package frc.robot.path;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public interface Waypoints {

	public Waypoint[] points();

	public final class CenterToLeftSwitch implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(65, 40, Pathfinder.d2r(30)),	// Convert radians to degrees: Pathfinder.d2r(45)
					new Waypoint(97, 60, Pathfinder.d2r(81))
			};
		}
	}

	public final class CenterToRightSwitch implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(70, -26, Pathfinder.d2r(-23)),	// Convert radians to degrees: Pathfinder.d2r(45)
					new Waypoint(97, -49, Pathfinder.d2r(-81))
			};
		}
	}

	public final class RightSideScale implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(291, 1, 0)
			};
		}
	}

	public final class LeftSideScale implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(291, -1, 0)
			};
		}
	}

	public final class RightSideSwitch implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(135, 17, 0)
			};
		}
	}

	public final class LeftSideSwitch implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(135, -17, 0)
			};
		}
	}
	
	public final class CrossBaseline implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(120, 0, 0)
			};
		}
	}
	// perameters are (forward, side, angle)
	public final class RightFarSideScalePlatform implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(152,-20, 0),
					new Waypoint(216.5, 42.4, Pathfinder.d2r(90)),
					new Waypoint(216.5, 137.5, Pathfinder.d2r(50)),
					//so it is straight before driving on the platform 
					new Waypoint(226.6, 137.5, Pathfinder.d2r(0)),
					new Waypoint(274.25, 145.07, Pathfinder.d2r(0)),
					new Waypoint(280.25, 145.07, Pathfinder.d2r(-20))
			};
		}
	}
	
	public final class LeftFarSideScalePlatform implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(152, 20, 0),
					new Waypoint(216.5, -42.4, Pathfinder.d2r(-90)),
					new Waypoint(216.5, -137.5, Pathfinder.d2r(-50)),
					//so it is straight before driving on the platform 
					new Waypoint(226.6, -137.5, Pathfinder.d2r(0)),
					new Waypoint(274.25, -145.07, Pathfinder.d2r(0)),
					new Waypoint(280.25, -145.07, Pathfinder.d2r(20))
			};
 		}
	}
	
	
}