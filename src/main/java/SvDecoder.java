import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapPacket;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Slf4j
public class SvDecoder {

    private static final int  datasetSize = 64;

    public Optional<SvPacket> decode(PcapPacket packet) {
        try {
            byte[] data = packet.getRawData();
            int length = data.length;

            SvPacket result = new SvPacket();

            result.setMacDst(byteArrayToMac(data,0));
            result.setMacSrc(byteArrayToMac(data,6));
            result.setPackType(byteArrayToHex(data, 12));
            result.setAppID(byteArrayToHex(data, 14));
            result.setSvID(byteArrayToSVID(data,33));
            result.setSmpCount(byteArrayToSmpCnt(data, 45));
            result.setConfRef(byteArrayToInt(data, 49));
            result.setSmpSynch(byteArrayToSmpSynch(data, 55));


            result.getDataset().setInstIa(byteArrayToInt(data, length - datasetSize) / 100.0);
            result.getDataset().setQIa(byteArrayToInt(data, length - datasetSize + 4));
            result.getDataset().setInstIb(byteArrayToInt(data, length - datasetSize + 8) / 100.0);
            result.getDataset().setQIb(byteArrayToInt(data, length - datasetSize + 12));
            result.getDataset().setInstIc(byteArrayToInt(data, length - datasetSize + 16) / 100.0);
            result.getDataset().setQIc(byteArrayToInt(data, length - datasetSize + 20));
            result.getDataset().setInstIn(byteArrayToInt(data, length - datasetSize + 24) / 100.0);
            result.getDataset().setQIn(byteArrayToInt(data, length - datasetSize + 28));
            result.getDataset().setInstUa(byteArrayToInt(data, length - datasetSize + 32) / 100.0);
            result.getDataset().setQUa(byteArrayToInt(data, length - datasetSize + 36));
            result.getDataset().setInstUb(byteArrayToInt(data, length - datasetSize + 40) / 100.0);
            result.getDataset().setQUb(byteArrayToInt(data, length - datasetSize + 44));
            result.getDataset().setInstUc(byteArrayToInt(data, length - datasetSize + 48) / 100.0);
            result.getDataset().setQUc(byteArrayToInt(data, length - datasetSize + 52));
            result.getDataset().setInstUn(byteArrayToInt(data, length - datasetSize + 56) / 100.0);
            result.getDataset().setQUn(byteArrayToInt(data, length - datasetSize + 60));


            return Optional.of(result);
        } catch (Exception e) {
            log.error("Cannot parse sv packet");
        }
        return Optional.empty();
    }
    public static String byteArrayToMac(byte[] b, int offset) {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                b[offset],
                b[1+offset],
                b[2+offset],
                b[3+offset],
                b[4+offset],
                b[5+offset]
        );
    }
    public static String byteArrayToHex(byte[] b, int offset) {
        return String.format("0x%02x%02x",
                b[offset],
                b[1+offset]
        );
    }

    public static String byteArrayToSVID(byte[] b, int offset) {
        byte[] byteArray = new byte[10];
        for (int i=0; i<10; i++){
            byteArray[i] = b[offset + i];
        }
        try {
            return new String(byteArray, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static int byteArrayToSmpCnt (byte[] b, int offset) {
        return b[offset + 1] & 0xFF | (b[offset] & 0xFF) << 8;
    }

    public static int byteArrayToSmpSynch (byte[] b, int offset) {
        return b[offset] & 0xFF;
    }

    public static int byteArrayToInt (byte[] b, int offset) {
        return b[offset + 3] & 0xFF | (b[offset + 2] & 0xFF) << 8 | (b[offset + 1] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
    }
}
