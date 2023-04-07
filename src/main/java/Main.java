import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        EthernetListener ethernetListener = new EthernetListener();
        ethernetListener.setNicName("Realtek 8822CE Wireless LAN 802.11ac PCI-E NIC");

        SvDecoder svDecoder = new SvDecoder();

        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        ethernetListener.addListener(packet -> {
            Optional<SvPacket> svPacket = svDecoder.decode(packet);
            if(svPacket.isPresent()) {
                if (svPacket.get().getSmpCount() != dataAnalyzer.getActualCnt()) {
                    DecimalFormat df = new DecimalFormat("#.####");
                    System.out.println("MacDestination: " + svPacket.get().getMacDst());
                    System.out.println("MacSource: " + svPacket.get().getMacSrc());
                    System.out.println("Packet Type: " + svPacket.get().getPackType());
                    System.out.println("AppID: " + svPacket.get().getAppID());
                    System.out.println("svID: " + svPacket.get().getSvID());
                    System.out.println("smpCnt: " + svPacket.get().getSmpCount());
                    System.out.println("confRef: " + svPacket.get().getConfRef());
                    System.out.println("smpSynch: " + svPacket.get().getSmpSynch());
                    System.out.println();

                    String[][] arrayForI = new String[4][2];

                    arrayForI[0][0] = "Ia = " + df.format(svPacket.get().getDataset().getInstIa() / 1000);
                    arrayForI[0][1] = "qIa = " + svPacket.get().getDataset().getQIa();
                    arrayForI[1][0] = "Ib = " + df.format(svPacket.get().getDataset().getInstIb() / 1000);
                    arrayForI[1][1] = "qIb = " + svPacket.get().getDataset().getQIb();
                    arrayForI[2][0] = "Ic = " + df.format(svPacket.get().getDataset().getInstIc() / 1000);
                    arrayForI[2][1] = "qIc = " + svPacket.get().getDataset().getQIc();
                    arrayForI[3][0] = "In = " + df.format(svPacket.get().getDataset().getInstIn() / 1000);
                    arrayForI[3][1] = "qIn = " + svPacket.get().getDataset().getQIn();

                    double Ia = svPacket.get().getDataset().getInstIa() / 1000;
                    double Ib = svPacket.get().getDataset().getInstIb() / 1000;
                    double Ic = svPacket.get().getDataset().getInstIc() / 1000;
                    double Ua = svPacket.get().getDataset().getInstUa() / 1000;
                    double Ub = svPacket.get().getDataset().getInstUb() / 1000;
                    double Uc = svPacket.get().getDataset().getInstUc() / 1000;

                    dataAnalyzer.checkNormMode(Ia, Ib, Ic, Ua, Ub, Uc);
                    dataAnalyzer.checkFaultMode(Ia, Ib, Ic, Ua, Ub, Uc);


                    for (int i = 0; i < arrayForI.length; i++) {
                        for (int j = 0; j < arrayForI[i].length; j++) {
                            System.out.print(arrayForI[i][j] + StringUtils.repeat(" ", 18 - arrayForI[i][j].length()));
                        }
                        System.out.println();
                    }
                    System.out.println();

                    String[][] arrayForU = new String[4][2];

                    arrayForU[0][0] = "Ua = " + df.format(svPacket.get().getDataset().getInstUa() / 1000);
                    arrayForU[0][1] = "qUa = " + svPacket.get().getDataset().getQUa();
                    arrayForU[1][0] = "Ub = " + df.format(svPacket.get().getDataset().getInstUb() / 1000);
                    arrayForU[1][1] = "qUb = " + svPacket.get().getDataset().getQUb();
                    arrayForU[2][0] = "Uc = " + df.format(svPacket.get().getDataset().getInstUc() / 1000);
                    arrayForU[2][1] = "qUc = " + svPacket.get().getDataset().getQUc();
                    arrayForU[3][0] = "Un = " + df.format(svPacket.get().getDataset().getInstUn() / 1000);
                    arrayForU[3][1] = "qUn = " + svPacket.get().getDataset().getQUn();

                    for (int k = 0; k < arrayForU.length; k++) {
                        for (int l = 0; l < arrayForU[k].length; l++) {
                            System.out.print(arrayForU[k][l] + StringUtils.repeat(" ", 18 - arrayForU[k][l].length()));
                        }
                        System.out.println();
                    }
                    System.out.println("-----------------------------------");

                    dataAnalyzer.setCounter(dataAnalyzer.getCounter() + 1);
                    if (dataAnalyzer.getCounter() == 12000) {
                        dataAnalyzer.printAnswer();
                        dataAnalyzer.setCounter(0);
                        dataAnalyzer.setActualCnt(0);
                        dataAnalyzer.setFaultDuration(0);
                        dataAnalyzer.setMaxNormVoltage(0);
                        dataAnalyzer.setMaxNormCurrent(0);
                        dataAnalyzer.setMaxFaultCurrent(0);
                        dataAnalyzer.faultTypes.clear();
                    }

                    System.out.println();

                    dataAnalyzer.setActualCnt(svPacket.get().getSmpCount());
                }
            }
        });
        ethernetListener.start();
    }
}
