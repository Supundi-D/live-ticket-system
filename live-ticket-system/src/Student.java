package studentmanagement;

public class Student {
    private String id;
    private String name;
    private Module[] modules = new Module[3];

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        for (int i = 0; i < 3; i++) {
            modules[i] = new Module();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setModuleMark(int moduleIndex, int mark) {
        if (moduleIndex >= 0 && moduleIndex < 3) {
            modules[moduleIndex].setMark(mark);
        }
    }

    public Module getModule(int moduleIndex) {
        if (moduleIndex >= 0 && moduleIndex < 3) {
            return modules[moduleIndex];
        }
        return null;
    }

    public double getAverage() {
        int total = 0;
        for (Module module : modules) {
            total += module.getMark();
        }
        return total / 3.0;
    }

    public String getGrade() {
        double average = getAverage();
        if (average >= 80) {
            return "Distinction";
        } else if (average >= 70) {
            return "Merit";
        } else if (average >= 40) {
            return "Pass";
        } else {
            return "Fail";
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", modules=[" + modules[0].getMark() + "," + modules[1].getMark() + "," + modules[2].getMark() + "]" +
                '}';
    }
}
