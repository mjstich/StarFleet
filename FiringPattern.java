public enum FiringPattern {
	alpha, beta, gamma, delta;

	static {
		alpha.firingPattern = new int[][] { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
		beta.firingPattern = new int[][] { { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 } };
		gamma.firingPattern = new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 } };
		delta.firingPattern = new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 } };
	}

    private int[][] firingPattern;

    public int[][] getFiringPattern() {
    	return firingPattern;
    }
}