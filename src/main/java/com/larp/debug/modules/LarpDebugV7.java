package com.larp.debug.modules;

import com.larp.debug.LarpDebug;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.Set;

public class LarpDebugV7 extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender  = settings.createGroup("Render");

    private final Setting<Integer> chunkHeight = sgGeneral.add(new IntSetting.Builder()
        .name("chunk-height")
        .description("Chunk box yüksekliği (blok).")
        .defaultValue(16)
        .min(1)
        .sliderMax(384)
        .build()
    );

    private final Setting<Integer> chunkYOffset = sgGeneral.add(new IntSetting.Builder()
        .name("chunk-y-offset")
        .description("Chunk box'ın başladığı Y koordinatı.")
        .defaultValue(0)
        .min(-64)
        .sliderMin(-64)
        .sliderMax(320)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("Chunk box çizim modu.")
        .defaultValue(ShapeMode.Lines)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("Chunk box yüzey rengi.")
        .defaultValue(new SettingColor(255, 200, 0, 30))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("Chunk box kenar rengi.")
        .defaultValue(new SettingColor(255, 200, 0, 200))
        .build()
    );

    private final Set<ChunkPos> notifiedChunks = new HashSet<>();

    public LarpDebugV7() {
        super(LarpDebug.CATEGORY, "LarpDebugV7", "Bal seviyesi 5 olan arı kovanlarını tarar ve chunk'larını işaretler.");
    }

    @Override
    public void onDeactivate() {
        notifiedChunks.clear();
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        ClientWorld world = mc.world;
        Set<ChunkPos> markedChunks = new HashSet<>();

        int renderDist = mc.options.getViewDistance().getValue();
        ChunkPos playerChunk = mc.player.getChunkPos();

        int startX = playerChunk.x - renderDist;
        int endX   = playerChunk.x + renderDist;
        int startZ = playerChunk.z - renderDist;
        int endZ   = playerChunk.z + renderDist;

        for (int cx = startX; cx <= endX; cx++) {
            for (int cz = startZ; cz <= endZ; cz++) {
                if (!world.isChunkLoaded(cx, cz)) continue;

                WorldChunk chunk = world.getChunk(cx, cz);
                final ChunkPos chunkPos = new ChunkPos(cx, cz);

                for (var entry : chunk.getBlockEntities().entrySet()) {
                    if (entry.getValue() instanceof BeehiveBlockEntity) {
                        BlockState state = world.getBlockState(entry.getKey());
                        if (state.contains(BeehiveBlock.HONEY_LEVEL) && state.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
                            markedChunks.add(chunkPos);
                            break;
                        }
                    }
                }
            }
        }

        for (ChunkPos chunkPos : markedChunks) {
            if (!notifiedChunks.contains(chunkPos)) {
                mc.world.playSound(
                    mc.player,
                    mc.player.getBlockPos(),
                    SoundEvents.BLOCK_BELL_USE,
                    SoundCategory.MASTER,
                    0.6f,
                    2.0f
                );
                mc.player.sendMessage(
                    Text.literal("Larp Founded !!! ( Best DEV - NUSA)"),
                    false
                );
            }
            renderChunk(event, chunkPos);
        }

        notifiedChunks.clear();
        notifiedChunks.addAll(markedChunks);
    }

    private void renderChunk(Render3DEvent event, ChunkPos chunkPos) {
        int x1 = chunkPos.getStartX();
        int z1 = chunkPos.getStartZ();
        int x2 = chunkPos.getEndX() + 1;
        int z2 = chunkPos.getEndZ() + 1;
        int y1 = chunkYOffset.get();
        int y2 = y1 + chunkHeight.get();

        Box box = new Box(x1, y1, z1, x2, y2, z2);

        event.renderer.box(
            box.minX, box.minY, box.minZ,
            box.maxX, box.maxY, box.maxZ,
            sideColor.get(),
            lineColor.get(),
            shapeMode.get(),
            0
        );
    }
}
