import lombok.Data;
@Data
public class SvPacket {
    private String macDst;
    private String macSrc;
    private String packType;
    private String appID;
    private String svID;
    private int smpCount;
    private int confRef;
    private int smpSynch;

    private Dataset dataset = new Dataset();

    @Data
    public class Dataset{
        private double instIa;
        private int qIa;
        private double instIb;
        private int qIb;
        private double instIc;
        private int qIc;
        private double instIn;
        private int qIn;

        private double instUa;
        private int qUa;
        private double instUb;
        private int qUb;
        private double instUc;
        private int qUc;
        private double instUn;
        private int qUn;
    }
}
