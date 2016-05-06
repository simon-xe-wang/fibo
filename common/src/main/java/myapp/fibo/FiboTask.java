package myapp.fibo;

public class FiboTask {

    final public static int STATE_READY = 0;
    final public static int STATE_INPROGRESS = 1;

    private String id;
    private int sn;
    private int state = STATE_INPROGRESS;
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public static FiboTask create(int sn) {
        return null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FiboTask{" +
                "id='" + id + '\'' +
                ", sn=" + sn +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiboTask fiboTask = (FiboTask) o;

        if (sn != fiboTask.sn) return false;
        if (state != fiboTask.state) return false;
        if (createTime != fiboTask.createTime) return false;
        return id.equals(fiboTask.id);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + sn;
        result = 31 * result + state;
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        return result;
    }
}
