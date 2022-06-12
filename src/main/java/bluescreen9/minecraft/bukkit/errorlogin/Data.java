package bluescreen9.minecraft.bukkit.errorlogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Data {
				protected static HashMap<String, Location> OriginalLocs = new HashMap<String, Location>();
				protected static Location loginPoint;
				
				private static HashMap<String, String> data = new HashMap<String, String>();
				private static byte[] update;
				private static ArrayList<UUID> logined_players = new ArrayList<UUID>();
				private static HashMap<String, Date> date = new HashMap<String, Date>();
				
				protected static void  register(Player player,String password) throws NoSuchAlgorithmException {
						data.put(md5(player.getUniqueId().toString()), md5(password));
						date.put(md5(player.getUniqueId().toString()), new Date());
						player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.success.message"), player));
						if (Main.ErrorLogin.getConfig().getBoolean("prompts.title")) {
							MessageUtil.sendTitle(player, MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.success.title.maintitle"), player),
									MessageUtil.dispose(Main.Language.getLang(player, "prompt.register.success.title.subtitle"),player), 20, 80, 60);
						}
						
						if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
						player.setAllowFlight(false);
						}
						Tone.yes(player);
						logined_players.add(player.getUniqueId());
						PlayerHider.showPlayer(player);
				}
				
				@SuppressWarnings("deprecation")
				protected static boolean login(Player player,String password) throws NoSuchAlgorithmException {
					String code = data.get(md5(player.getUniqueId().toString()));
					if (!isRegistered(player)) {
						player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.warning.unregister"), player));
						Tone.no(player);
						return false;
					}
					if (isLogined(player)) {
						player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.alreadylogined"), player));
						Tone.no(player);
						return false;
					}
					if (!code.equals(md5(password))) {
						if (Main.ErrorLogin.getConfig().getBoolean("player-kicks.wrongpassword")) {
							player.kickPlayer(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.wrongpassword"), player));
						}else {
							Tone.no(player);
							player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.wrongpassword"), player));
						}
						return false;
					}
					logined_players.add(player.getUniqueId());
					player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.success.message"), player));
					if (Main.ErrorLogin.getConfig().getBoolean("prompts.title")) {
						MessageUtil.sendTitle(player, MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.success.title.maintitle"), player),
								MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.success.title.subtitle"), player),
								20, 80, 60);
					}
					date.replace(md5(player.getUniqueId().toString()), new Date());
					
					if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
						player.setAllowFlight(false);
						}
					if (OriginalLocs.containsKey(player.getUniqueId().toString())) {
						player.teleport(OriginalLocs.get(player.getUniqueId().toString()));
					}
					Tone.yes(player);
					PlayerHider.showPlayer(player);
					return true;
				}
				
				protected static boolean isLogined(Player player) {
					if (!logined_players.contains(player.getUniqueId())) {
						return false;
					}
					return true;
				}
				
				protected static boolean isRegistered(Player player) {
					try {
						if (data.get(md5(player.getUniqueId().toString())) == null) {
							return false;
						}
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				}
				
				protected static void loadData() {
						try {
							File dataFolder = Main.ErrorLogin.getDataFolder();
							if (!dataFolder.exists()) {
								dataFolder.mkdirs();
							}
							if (!dataFolder.isDirectory()) {
								dataFolder.delete();
								dataFolder.mkdirs();
							}
							File dataFile = new File(dataFolder, "data.json");
							if (!dataFile.exists() || dataFile.length() == 0) {
								return;
							}
							FileInputStream in = new FileInputStream(dataFile);
							JSONObject jsonObject = JSON.parseObject(new String(in.readAllBytes(),Charset.forName("utf-8")));
							in.close();
							
							for (String str:jsonObject.keySet()) {
									data.put(str, jsonObject.getJSONObject(str).getString("code"));
									date.put(str, new Date(jsonObject.getJSONObject(str).getLong("time")));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				
				protected static void saveData() {
					if (update == null) {
						return;
					}
						try {
							File dataFolder = Main.ErrorLogin.getDataFolder();
							if (!dataFolder.exists()) {
								dataFolder.mkdirs();
							}
							if (!dataFolder.isDirectory()) {
								dataFolder.delete();
								dataFolder.mkdirs();
							}
							File dataFile = new File(dataFolder, "data.json");
							if (!dataFile.exists()) {
								dataFile.createNewFile();
							}
							
							FileOutputStream out = new FileOutputStream(dataFile);
							out.write(update);
							out.flush();
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				
				protected static void updateData() {
						JSONObject jsonObject = new JSONObject();
						for (String str:data.keySet()) {
							jsonObject.put(str, new JSONObject());
							JSONObject jsonObject2 = jsonObject.getJSONObject(str);
							jsonObject2.put("code", data.get(str));
							jsonObject2.put("time", new Date().getTime());
						}
						update = jsonObject.toJSONString().getBytes(Charset.forName("utf-8"));
				}
				
			    private static String md5(String data) throws NoSuchAlgorithmException {
			        StringBuilder sb = new StringBuilder();
			            MessageDigest md = MessageDigest.getInstance("md5");
			            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));
			            for (byte b : md5) {
			                sb.append(Integer.toHexString(b & 0xff));
			            }
			        return sb.toString();
			    }
			    
			    public static Date getLoginDate(Player player) {
			    	try {
						return date.get(md5(player.getUniqueId().toString()));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
			    	return null;
			    }
			    
			    public static void autoLogin(Player player) {
			    	logined_players.add(player.getUniqueId());
							player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.autologin.message"), player));
							if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
								player.setAllowFlight(false);
								}
							if (Main.ErrorLogin.getConfig().getBoolean("prompts.title")) {
					    		MessageUtil.sendTitle(player, MessageUtil.dispose(Main.Language.getLang(player, "prompt.autologin.title.maintitle"), player),
					    				MessageUtil.dispose(Main.Language.getLang(player, "prompt.autologin.title.subtitle"), player),
					    				20, 40, 30);
					    	}
					    	try {
								date.put(md5(player.getUniqueId().toString()), new Date());
							} catch (NoSuchAlgorithmException e) {
								e.printStackTrace();
							}
					    	PlayerHider.showPlayer(player);
					    	Tone.yes(player);
			    }
			    
			    protected static void resetPassword(Player player,String oldpassword,String newpassword) throws NoSuchAlgorithmException {
			    	if (!md5(oldpassword).equals(data.get(md5(player.getUniqueId().toString())))) {
			    		player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.wrongoldpassword"), player));
			    		Tone.no(player);
			    		return;
			    	}
			    	if (!isSecurePassword(newpassword, player)) {
			    		player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.insecurepassword"), player));
			    		Tone.no(player);
			    		return;
			    	}
			    	data.replace(md5(player.getUniqueId().toString()), md5(newpassword));
			    	Tone.yes(player);
			    	player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.resetpassword.success"), player));
			    	date.put(md5(player.getUniqueId().toString()), new Date());
			    }
			    
			    public static boolean isSecurePassword(String password,Player player) {
			    	if (Main.ErrorLogin.getConfig().getBoolean("allow-insecure-password")) {
			    		return true;
			    	}
			    	if (password.length() < 6) {
			    		return false;
			    	}
			    	if (password.equals(player.getName())) {
			    		return false;
			    	}
			    	
			    	if (!PasswordCheck.checkContainDigit(password)) {
			    		return false;
			    	}
			    	
			    	if (!PasswordCheck.checkContainCase(password)) {
			    		return false;
			    	}
			    	if (PasswordCheck.checkLateralKeyboardSite(password, 5, true)) {
			    		return false;
			    	}
			    	if (PasswordCheck.checkKeyboardSlantSite(password, 5, true)) {
			    		return false;
			    	}
			    	return true;
			    }
			    
			    public static boolean isMoangPlayer(Player player) {
			    	if (!Main.ErrorLogin.getConfig().getBoolean("skip-genuine-player")) {
			    		return false;
			    	}
			    	
			    	try {
						URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName());
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						con.setRequestProperty("User-Agent", "Mozilla/5.0");
						if (con.getResponseCode() == 204) {
							return false;
						}
						JSONObject data = JSON.parseObject(new String(con.getInputStream().readAllBytes(),Charset.forName("utf-8")));
						if (data.getString("id").equals(player.getUniqueId().toString())) {
							return true;
						}
					} catch (Error | Exception e) {
						e.printStackTrace();
					}
			    	return false;
			    }
			    
			    protected static void loginMojangPlayer(Player player) {
			    	logined_players.add(player.getUniqueId());
			    	player.sendMessage(MessageUtil.dispose(Main.Language.getLang(player, "prompt.login.skipgenuine"), player));
			    	Tone.yes(player);
			    }
			    
			    public static void quit(Player player) {
						logined_players.remove(player.getUniqueId());
						if (!isLogined(player)) {
							return;
						}
						try {
							date.put(md5(player.getUniqueId().toString()), new Date());
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
			    }
}
