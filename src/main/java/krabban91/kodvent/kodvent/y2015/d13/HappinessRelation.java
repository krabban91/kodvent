package krabban91.kodvent.kodvent.y2015.d13;

public class HappinessRelation {

    private final HappinessDelta one;
    private final HappinessDelta two;
    private final String personOne;
    private final String personTwo;
    private final int change;

    public HappinessRelation(HappinessDelta one, HappinessDelta two) {
        this.change = one.getChange()+two.getChange();
        assert one.getPerson().equals(two.getNextTo());
        assert two.getPerson().equals(one.getNextTo());
        this.one = one;
        this.two = two;
        this.personOne = one.getPerson();
        this.personTwo = two.getPerson();
    }

    public int getChange() {
        return change;
    }

    public String getPersonOne() {
        return personOne;
    }

    public String getPersonTwo() {
        return personTwo;
    }

    public HappinessDelta getOne() {
        return one;
    }

    public HappinessDelta getTwo() {
        return two;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HappinessRelation that = (HappinessRelation) o;
        return change == that.change &&
                (personTwo.equals(that.personTwo) || personTwo.equals(that.personOne)) &&
                (personOne.equals(that.personTwo) || personOne.equals(that.personOne));
    }

    @Override
    public int hashCode() {
        int result = personOne.hashCode() * personTwo.hashCode();
        result = 31 * result + change;
        return result;
    }
}
