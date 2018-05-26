package com.chocorean.authmod.authentification;

public class FileAuthenticationStrategy implements IAuthenticationStrategy {
    @Override
    public boolean login(String user, String password) throws Exception {
        /*String hash = RegisterCommand.generateHash(sender.getName());
        String pwd = RegisterCommand.generateHash(args[0]);
        try {
            BufferedReader br = new BufferedReader(new FileReader("mods/AuthMod/data"));
            String line=br.readLine();

            while (line != null) {
                if (line.contains(hash)) {
                    if (line.contains(pwd)){
                        // Free player here
                        for (PlayerDescriptor dc : Handler.desc) {
                            if (dc.getPlayer().getName().equals(sender.getName())){
                                Handler.desc.remove(dc);
                                sender.addChatMessage(new TextComponentString("Logged in successfully."));
                                ((EntityPlayerMP)sender).setPositionAndUpdate(dc.getPos().getX(),dc.getPos().getY(),dc.getPos().getZ());
                                return;
                            }
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.addChatMessage(new TextComponentString("Wrong password. Try again."));
        */
        return false;
    }
}
