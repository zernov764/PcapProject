import lombok.Data;

import java.util.ArrayList;

@Data
public class DataAnalyzer {

    public int actualCnt;
    public double maxNormCurrent;
    public double maxNormVoltage;
    public double maxFaultCurrent;
    public double maxFaultVoltage;
    public int faultDuration;
    public ArrayList<String> faultTypes = new ArrayList<>();
    public int counter;

    public void checkNormMode(double ia, double ib, double ic, double ua, double ub, double uc){

        double Ia = Math.abs(ia);
        double Ib = Math.abs(ib);
        double Ic = Math.abs(ic);
        double maxI = Math.max(Math.max(Ia,Ib),Ic);
        double Ua = Math.abs(ua);
        double Ub = Math.abs(ub);
        double Uc = Math.abs(uc);
        double maxU = Math.max(Math.max(Ua,Ub),Uc);

        if (counter < 201){
            if (counter == 200){
                faultTypes.add("Нормальный режим");
            }
            if (maxI > getMaxNormCurrent()){
                setMaxNormCurrent(maxI);
            }
            if (maxU > getMaxNormVoltage()){
                setMaxNormVoltage(maxU);
            }
        }
    }
    public void checkFaultMode(double ia, double ib, double ic, double ua, double ub, double uc){

        double Ia = Math.abs(ia);
        double Ib = Math.abs(ib);
        double Ic = Math.abs(ic);
        double maxI = Math.max(Math.max(Ia,Ib),Ic);
        double Ua = Math.abs(ua);
        double Ub = Math.abs(ub);
        double Uc = Math.abs(uc);
        double maxU = Math.max(Math.max(Ua,Ub),Uc);

        if (maxI > getMaxFaultCurrent()){
            setMaxFaultCurrent(maxI);
        }
        if (maxU > getMaxFaultVoltage()){
            setMaxFaultVoltage(maxU);
        }
        if (counter > 201){
            double setpoint = getMaxNormCurrent() * 8;
            int faultPhases = 0;
            if (Ia > setpoint || Ib > setpoint || Ic > setpoint){
                setFaultDuration(getFaultDuration() + 1);
            }
            if (Ia > setpoint){faultPhases += 1;}
            if (Ib > setpoint){faultPhases += 1;}
            if (Ic > setpoint){faultPhases += 1;}
            if (faultPhases == 1){
                boolean found = getFaultTypes().contains("Однофазное КЗ");
                if (!found){
                    faultTypes.add("Однофазное КЗ");
                }
            }
            if (faultPhases == 2){
                boolean found = getFaultTypes().contains("Двухфазное КЗ");
                if (!found){
                    faultTypes.add("Двухфазное КЗ");
                }
            }
            if (faultPhases == 3){
                boolean found = getFaultTypes().contains("Трехфазное КЗ");
                if (!found){
                    faultTypes.add("Трехфазное КЗ");
                }
            }
        }
    }
    public void printAnswer(){
        System.out.println("Амплитудное значение фазного тока в нормальном режиме: " + getMaxNormCurrent());
        System.out.println("Амплитудное значение фазного напряжения в нормальном режиме: " + getMaxNormVoltage());
        System.out.println();
        System.out.println("Минимальный ток КЗ принят равным 8 * Iнорм.макс");
        System.out.println("Максимальный ток аварийного режима: " + getMaxFaultCurrent());
        System.out.println("Максимальное напряжение аварийного режима: " + getMaxFaultVoltage());
        System.out.println("Длительность аварийного режима: " + getFaultDuration() + " фреймов");
        System.out.println("Виды режимов: " + getFaultTypes());
    }
}