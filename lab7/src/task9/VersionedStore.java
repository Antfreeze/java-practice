package task9;

import java.io.*;
import java.nio.file.*;
import java.util.zip.CRC32;

public class VersionedStore {
    public static final int V1 = 1, V2 = 2;
    public static final int CURRENT = V2;

    private static long checksumV1(String name) {
        return checksum(name, null);
    }

    private static long checksumV2(String name, int val) {
        return checksum(name, val);
    }

    private static long checksum(String name, Integer value) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(bos)) {
            out.writeUTF(name);
            if (value != null)
                out.writeInt(value);
            CRC32 crc = new CRC32();
            crc.update(bos.toByteArray());
            return crc.getValue();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writeV1(Path file, String name) throws IOException {
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(V1);
            out.writeUTF(name);
            out.writeLong(checksumV1(name));
        }
    }

    public void writeV2(Path file, String name, int value) throws IOException {
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(V2);
            out.writeUTF(name);
            out.writeInt(value);
            out.writeLong(checksumV2(name, value));
        }
    }

    public Data readAndMigrate(Path file) throws IOException {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int version = in.readInt();
            if (version > CURRENT)
                throw new IOException("Версия из будущего: " + version);
            if (version == V1) {
                String name = in.readUTF();
                if (in.readLong() != checksumV1(name))
                    throw new IOException("Checksum V1 не совпал");
                return new Data(name, 0);
            }
            if (version == V2) {
                String name = in.readUTF();
                int value = in.readInt();
                if (in.readLong() != checksumV2(name, value))
                    throw new IOException("Checksum V2 не совпал");
                return new Data(name, value);
            }
            throw new IOException("Неизвестная версия: " + version);
        } catch (EOFException e) {
            throw new IOException("Файл обрезан", e);
        }
    }
}
