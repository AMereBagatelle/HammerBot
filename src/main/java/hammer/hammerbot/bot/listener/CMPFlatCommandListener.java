package hammer.hammerbot.bot.listener;

import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.io.File;

public class CMPFlatCommandListener extends MainCommandListener {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.startsWith("/")) {
            String formattedContent = content.substring(1);
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById(SettingsManager.INSTANCE.loadSettingOrDefault("memberRoleId", "")))) {
                if (formattedContent.startsWith("upload")) {
                    for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                        if (attachment != null) {//sanity check
                            uploadFile(attachment);
                        }
                    }
                }
            }
        }
    }

    public void uploadFile(Message.Attachment fileAttachment) {
        String fileExtension = fileAttachment.getFileExtension();
        if (fileExtension.equals("sc")) {
            File downloadPath = new File("world/scripts/" + fileAttachment.getFileName());
            fileAttachment.downloadToFile(downloadPath);
        }
    }
}
