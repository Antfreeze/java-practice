package task5;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BinaryStore {
    private static final int MAGIC = 0xCAFEBABE;
    private static final int VERSION = 1;

    public void write(Path file, List<String> data) throws IOException {
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(data.size());
            for (String s : data)
                out.writeUTF(s);
        }
    }

    public List<String> read(Path file) throws IOException {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            if (in.readInt() != MAGIC)
                throw new IOException("Не наш файл (magic)");
            int version = in.readInt();
            if (version != VERSION)
                throw new IOException("Версия не поддерживается: " + version);
            int count = in.readInt();
            if (count < 0)
                throw new IOException("Повреждённое count: " + count);
            List<String> result = new ArrayList<>(count);
            for (int i = 0; i < count; i++)
                result.add(in.readUTF());
            return result;
        } catch (EOFException e) {
            throw new IOException("Файл обрезан", e);
        }
    }
}
