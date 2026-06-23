package domain;

public class Dial {
    private int position = 50;

    public void rotate(Rotation r) {
        int steps = r.steps();
        switch (r.direction()) {
            case 'L':
                position = (position - steps % 100 + 100) % 100;
                break;
            case 'R':
                position = (position + steps) % 100;
                break;
        }
    }
    public int getPosition() {
        return position;
    }
}
