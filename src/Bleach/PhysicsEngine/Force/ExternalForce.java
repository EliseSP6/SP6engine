package Bleach.PhysicsEngine.Force;


public class ExternalForce {

	public static enum ForceIdentifier {
		GRAVITY, WIND, JUMP;
	}

	private Force force;
	private boolean isExhausted = false;

	public ExternalForce(double vectorAngle, double deltaVelocity) {
		this.force = new Force(vectorAngle, deltaVelocity);
	}

	public double getMagnitude(double deltaTime) {
		double magnitude = force.getMagnitude(deltaTime);

		double newVelocity = force.getVelocity() - magnitude;
		if (newVelocity <= Double.MIN_NORMAL) {
			magnitude = magnitude - newVelocity;
			isExhausted = true;
			force.setVelocity(Double.MIN_NORMAL);
		}

		force.setVelocity(newVelocity);
		return magnitude;
	}

	public double getVectorAngle() {
		return force.getVectorAngle();
	}

	public boolean isExhaused() {
		return isExhausted;
	}
}