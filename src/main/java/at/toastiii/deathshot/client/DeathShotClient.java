package at.toastiii.deathshot.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Environment(EnvType.CLIENT)
public class DeathShotClient implements ClientModInitializer {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");


    @Override
    public void onInitializeClient() {

    }

    public static void saveScreenShot() {
        NativeImage nativeImage = ScreenshotUtils.takeScreenshot(0, 0, MinecraftClient.getInstance().getFramebuffer());
        File file = new File(MinecraftClient.getInstance().runDirectory, "death-shots");
        file.mkdir();
        File file2 = getScreenshotFilename(file);
        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeFile(file2);
                MutableText text = new LiteralText(file2.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath())));
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("Took death-shot: ").append(text));

            }
            catch (Exception text) {
                LogManager.getLogger().warn("Couldn't save death-screenshot", (Throwable)text);
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("Couldn't save death-screenshot..."));
            }
            finally {
                nativeImage.close();
            }
        });
    }

    private static File getScreenshotFilename(File directory) {
        String string = DATE_FORMAT.format(new Date());
        int i = 1;
        File file;
        while ((file = new File(directory, string + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
            ++i;
        }
        return file;
    }
}
