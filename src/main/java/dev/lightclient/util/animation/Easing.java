package dev.lightclient.util.animation;

/** A collection of easing functions used by the animation system. */
public enum Easing {
    LINEAR {
        @Override
        public double apply(double t) {
            return t;
        }
    },
    EASE_IN_OUT_QUAD {
        @Override
        public double apply(double t) {
            return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
        }
    },
    EASE_OUT_CUBIC {
        @Override
        public double apply(double t) {
            return 1 - Math.pow(1 - t, 3);
        }
    },
    EASE_OUT_EXPO {
        @Override
        public double apply(double t) {
            return t >= 1.0 ? 1.0 : 1 - Math.pow(2, -10 * t);
        }
    },
    EASE_OUT_BACK {
        @Override
        public double apply(double t) {
            double c1 = 1.70158;
            double c3 = c1 + 1;
            return 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
        }
    },
    SPRING {
        @Override
        public double apply(double t) {
            return 1 - Math.cos(t * Math.PI * (0.2 + 2.5 * t * t * t))
                    * Math.pow(1 - t, 2.2);
        }
    };

    public abstract double apply(double t);
}
