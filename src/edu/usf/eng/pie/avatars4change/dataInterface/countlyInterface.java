package edu.usf.eng.pie.avatars4change.dataInterface;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import ly.count.android.api.Countly;

public class countlyInterface {
	private static Handler delayHandle;
	private static final String TAG = "dataInterface.countlyInterface";
	
	// time between data updates
	private static final long DELAY_TIME = 10000; //TODO: this should be a setting?
	
	private static boolean continuePosting = true;

	public static void startSendingData(){
		continuePosting = true;
		delayHandle = new Handler();
		scheduleDataPost();
	}
	
	public static void resumeSendingData(){
		continuePosting = true;
		scheduleDataPost();
	}
	
	public static void stopSendingData(){
		continuePosting = false;
	}
	
	private static void scheduleDataPost(){
		delayHandle.postDelayed(new Runnable() {
			@Override
			public void run() {
				 postCountlyPAData();
				 if (continuePosting){
					 scheduleDataPost();
				 }
			}
		},DELAY_TIME);
	}
	
	// posts physical activity data to be sent to countly server
	private static void postCountlyPAData(){
		//Log.v(TAG,"queuing event physicalAcitivtyLevel = " + Float.toString(avgLevel));
		HashMap<String, String> segmentation = new HashMap<String,String>();
		segmentation.put("UID",userData.USERID);
		Countly.sharedInstance().recordEvent("physicalActivity",segmentation,1, userData.recentAvgActivityLevel);
	}
	
	// posts avatar view data to be sent to countly server
	public static void postCountlyViewData(final double amount){
		HashMap<String, String> segmentation = new HashMap<String,String>();
		segmentation.put("UID",userData.USERID);
		Countly.sharedInstance().recordEvent("avatarViewTime",segmentation,1, amount);
	}
	
	//sends image to database
	public static void sendImage(String imgPath){
		//convert image at given path to base64 string
		Bitmap bm = BitmapFactory.decodeFile(imgPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
		byte[] byteArrayImage = baos.toByteArray(); 
		String imgData = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		
		//URL-encode the base64 (i.e. replace + / and =)
		try {
			imgData = URLEncoder.encode(imgData, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG,"ERR: unsupported encoding to URLEncoder");
			e1.printStackTrace();
			return;
		}
		
		Log.d(TAG,"dataLen="+Integer.toString(imgData.length()));
		//request should look like: 
		// http://testsubdomain.socialvinesolutions.com:9001/?uid=tylarrr&data=iVBORw0KGgoAAAANSUhEUgAAAf8AAAJICAIAAADgtHq5AABAXElEQVR4nO3dfXRV1Z3%2FcRLCUwiSKmqAIEGCQslaIIaHrmANEiUuoA0ICg6IIwxi5bGmPtSUqakKFZVBF2MFbCoMMm2KmUWo6ARHUdGig8EJz0EiQkUIzwFSCJDfd3EW93ebx3Nu9jn37H3frz9cJ%2Bees%2Fe%2Be3%2Fv55x7c4nNqgEAkadZuAcAAAgDt9K%2FGeA%2FLlU7oCMX09%2BlloHQUJNAMNIfkYKaBIKR%2FogU1CQQjPRHpKAmgWCkf9hUVVUdOHDg0qVLtR%2F67rvvysvLvR%2BS2ahJIBjp31SnT58OfKWkefPm6enpb7zxhuyvrKys%2FZ2TwsJCeaiiomLatGmtWrWSPe3atXvwwQfPnj1rtZaXl9e%2Ff%2F%2BoqKjo6Oi0tLR169a5N%2FL58%2BcXFBS4177fRE5NAnaQ%2Fk1lpX%2B3bt2ee%2B65p556qnv37hLc%2B%2Ffvt9L%2FxhtvnBtk586dcsrkyZPloZtuumnevHl33nmnbGdlZcn%2BXbt2ybmS%2Fn%2F%2B85%2FlMhAbG9u2bdvjx4%2B7NHK5Vo0dO9alxn0ocmoSsIP0byor%2FeWW3%2FrxT3%2F6k%2Fz4wgsvWOkv4V7j%2BNLSUrm1l6vFhQsXrD0jRozo2rXrsWPH5E5cTpFLgrX%2FP%2F%2FzP2fNmvX111%2FL9pIlS%2Fr06SOHjRs3bvPmzdYB77zzzi233CIXmLfffnvQoEGvvPKK7JS3F%2F369evYseOYMWOOHDkie7Zv3y5vIxYvXjxq1Kibb775iSeeuHTp0qRJk2QYCQkJck1yf5J8IXJqErCD9G%2BqGun%2F9NNPB6d%2FYmLiz674xS9%2BIQdYEf%2FAAw%2FUbkqCPiYmpkWLFsOHD5ewLisrs%2FbLewI5pW%2FfvnIxkDcEKSkpsvPMmTOtWrVq167dsGHD2rRpIwc89thjJSUl8u5B3n9MnDixdevWAwYMkCM%2F%2F%2FxzeVSave222yTuZfujjz7Kzc2V9O%2FRo8fSpUu9maiwi5yaBOwg%2FZvKSn%2FJ3%2BTk5Ouvv97KWbndrv25%2FzXXXCPHyx26bL%2F00kt1trZq1aqkpCTreMlxCXHZuW3btoULF%2B7bt08uA%2FKoxLrs%2FMtf%2FiLHLF%2B%2BXLaXLVsm2z%2F%2F%2Bc%2FnzJkjG5s2bTp79uzUqVNle%2BvWrVb633fffXKkPCTbcvtfzSc%2FQGQj%2FZvKSv%2F4%2BPg77rjj7rvvlrv%2B3bt3V1%2F5re%2FQoUPPXXH%2B%2FHnZn5%2BfX%2BPev6Ki4vDhw4EPgoREtlwe5MZcjty4caPc5k%2BaNEm6kLyWm3255ZdjHn%2F8cXl0x44dsr1z504r%2FbOysmpcctasWWOl%2F%2Fz586svf%2B4k2zNnzqwm%2FYHIRvo3VY1PfgLq%2B9zfSuouXboE4r53796JiYlyefjjH%2F84YMCAd99919r%2FX%2F%2F1X3JkTk5Obm6u9WnSqVOn0tLSrPR%2F8803Zad18OrVq630t36fvH79%2BtIr5NJipf%2BCBQuqSX8AV5D%2BTdVw%2BkusTwsid%2BLy0KhRo%2BShnj17%2Fva3v01NTZXtV199VfZv3rw5Ojr6hhtueP755%2BUtglwV5KG1a9dat%2FnyJuC9996Liopq2bKlHCxvF%2BRgeX8g14Zu3bpZ6b9ixQrZmDBhwoYNG4YMGdK5c%2BcTJ07Ul%2F4tWrQYOHCg9U4lEkROTQJ2kP5N1XD61%2FCrX%2F1KHjp69Og999wT2HnffffJwdZZEt%2B33357TEyM7L%2F11lsXLlxYfflLO506dbK%2BJDp06FDZsL4IJI9ee%2B21CQkJM2bMkJ1ykZCdc%2BfOlUuO%2FBgXF7dy5crqK7%2F1rZ3%2B8kYhNjZ2%2BPDh3k1WWEVOTQJ2kP5hc%2FLkSbnZl1v42g%2FJDfvf%2Fva34D0XL1785ptvgvfs379f3kns3bu3%2BsonP9ZvgK2Dd%2B3ade7cuUbHcP78%2BYqKitCfg1aoSSAY6a%2Br48ePy43%2FNddcI287oqOjZVuuB%2BEelK9Rk0Aw0l9jcuO%2FZMmSWbNm%2Fdu%2F%2FduBAwfCPRy%2FoyaBYKQ%2FIgU1CQQj%2FREpqEkgGOmPSEFNAsFIf0QKahIIRvojUlCTQDAX0x%2FwG5eqHdAR9%2F6IFNQkEIz0R6SgJoFgpD8iBTUJBCP9ESmoSSAY6Y9IQU0CwUh%2FRApqEggWnvQPy7f9EAlCrkkg0oQt%2FV3qF5GMqgPsI%2F1hDqoOsI%2F0hzmoOsA%2B0h%2FmoOoA%2B0h%2FPzp06NCGDRsuXrwo21VVVQcOHLh06VK4B6UBqg6wj%2FS35dNPP5Uxz5o1q%2BlNzZ8%2Fv6CgoOFjHn300VtuuaWiomLatGmtWrWSrtu1a%2Ffggw%2BePXvWje5UnRV2hlUd4CrS3xaF6d%2B8efOxY8c2cMD58%2Bc7dOjw8ssvT548WTq96aab5s2bd%2Bedd8p2VlaW8u4UnhV2hlUd4CrS35ZA%2Bm%2Ffvj0tLW3x4sWjRo26%2Beabn3jiiUuXLn3%2B%2BeeDBg1asmTJ6NGjk5OT586de%2BHCBWvnqlWr5PRdu3bJ9rJlyyZNmhQVFZWQkCDHyP7Vq1cPGzYsMTFxwoQJe%2Fbssfpas2aNhK%2F0KEd269ZNmrL2jxgxomvXrseOHZPtL7%2F8UoYhbwh69eol%2FcqeOgdWo7vCwsJ%2B%2Ffp17NhxzJgxR44ckT1yrpz1%2B9%2F%2FXrYfeuih2267bd%2B%2BfTXO0ohhVQe4ivS3JZD%2Bkumy0aJFCwlKyUfZ%2Fuijj4qKimSjVatWQ4cObdu2rWxLCls75Ra%2B%2BnJYy%2Favf%2F3r3NxcCdYePXosXbr00KFDLVu2HDlyZE5OTvv27e%2B%2F%2F36rL7npzszMLCgokFMeeOCB2oM5evSoHC%2FdjR8%2F3hqDHFznwIK7KykpiY6O7t69%2B8SJE1u3bj1gwABp6vjx49dff%2F2111779ttvyynTpk2TncFneTjHChhWdYCrSH9baqT%2FfffdJzs3bdok23KXbQX9ww8%2FLDtLS0tle%2FDgwXWmf3XQhypW4Mr2xx9%2FvHHjxrVr18rOEydOSDSvXLnylVdekUdfeuml2oNZtGiRdYGR7b1798p2RkZGnQML7m7OnDmyUx46e%2Fbs1KlTZXvr1q2y%2F6233pJtuQ7JewLp3eqCT34A45H%2BttRI%2F%2Fnz51dfCfqZM2daQZ%2BXl2cd3KVLl27dugWn%2F%2BbNm2un%2F%2BnTp1NTU62%2FTyCnrFixQnYuW7YsLi7uzJkz%2Bfn5Ne79KyoqDh8%2BfOHChRkzZshDxcXF1n65009KSqpzYMHdZWVl1fijCGvWrLFa6N27t%2Fz44osvBvoi%2FQHjkf621Ej%2FBQsWVNdKfwll2SnBHRMT079%2Ff7mjl53PP%2F%2B87Hz33Xdrp%2F%2BxY8e%2B%2Ffbb999%2Ff%2Fbs2VdffXWbNm3k1vv222%2BfNGmSPLpz507rqhD43F8yOjEx8dy5c7m5ufKQXB5k58mTJ6VB6a7OgQV3Z%2F0Oef369aVXyOVE9n%2F44YdRUVHWr5f%2F%2Fve%2FW32R%2FoDxSH9b7KT%2Ftddeu2rVqnvvvdf61OXQoUOy0bNnz9dff7179%2B6B9G%2FRosXAgQN37969cuVK2fncc8%2Ft2bNn9OjRV1111fbt2yWIpTWr01GjRlkt%2FPa3v7XeJbz66quyf8uWLXJYnz59Vq9ePX78eNn%2F7LPP1pf%2Bge7kvYXsnDBhwoYNG4YMGdK5c2e52FRWVvbo0aN9%2B%2FZPPfWUPJqTk2N1HTgrLLMdMsOqDnAV6W%2BLnfS%2F%2B%2B6727VrJxtpaWlHjx6tvny7LTEt99HWfbeV%2FrIdGxs7fPhwuYuXfLd%2BSyxZvGjRInmjIBvWP%2FKqvvzb3XvuuSfwQc19990nYW09JAe3bNnS2i836fKGo770D3Qn23PnzpV3D%2FJQXFycXHtkjxX6L7zwggzmxhtvlNAvKSmpcZZGDKs6wFWkvwJW%2Br%2FxxhuSoQcOHAh%2B6OTJk6dOnapx%2FPnz561PXYScEviuZ52khc2bNx8%2BfLjGfrkSSFJbX9xsWHB3cmnZtWuXdOroLF1EVNUBTUT6KxBI%2F3APJNJFVNUBTUT6KyA37zk5OXKHHu6BRLqIqjqgiUh%2FmIOqA%2Bwj%2FWEOqg6wj%2FSHOag6wD7SH%2Bag6gD7SH%2BYg6oD7Atb%2BgNuCLkmgUjDvT%2FMQdUB9pH%2BMAdVB9hH%2BsMcVB1gH%2BkPc1B1gH2kP8xB1QH2kf4wB1UH2Ef6wxxUHWAf6Q9zUHWAfaQ%2FzEHVAfaR%2FjAHVQfYR%2FrDHFQdYB%2FpD3NQdYB9pD%2FMQdUB9pH%2BMAdVB9hH%2BsMcVB1gH%2BkPc1B1gH2kP8xB1QH2kf4wB1UH2Ef6wxxUHWAf6Q9zUHWAfaQ%2FzEHVAfaR%2FjAHVQfYR%2FrDHFQdYB%2FpD3NQdYB9pD%2FMQdUB9pH%2BMAdVB9hH%2BsMc9dXVwYMHG3gUiEykP8xRu66qqqoWLlxo7afqgGCkP8xRo67WrVuXkpIybdo00h%2BojfSHOQJ1tWPHjszMzPT09OLi4sB%2Bqg4IRvrDHFJX5eXl06dPT05OLigoqKqqCuyvpuqAf0T6wxzNrigsLKxzP7SQmJg4ePDgJ5988rPPPgtXLUUC0h%2FmkLrav3%2B%2FpEaHDh1SUlKWLl1aUVFRzb2%2FbmQR5fo9e%2FZseQ%2BXkZFRVlYW7hGZifSHOQJ1VVlZmZeXl5qaGh8fn52dTfprqqqqSpZPFrHGmzkoQfrDHLXr6oMPPhgzZgzpr7WioiK5AJSWloZ7IKYh%2FWGO%2BuqKf%2B2luyeffDIjIyPcozAN6Q9zUHWmqqqqSk5O5pfAapH%2BMAdVZ7DZs2dnZ2eHexRGIf1hDqrOYIWFhYMHDw73KIxC%2BsMcVJ3B9u%2Ffn5iYGO5RGIX0hzmoOrOxgmqR%2FjAHVWc2VlAt0h%2FmoOrMxgqqRfrDHFSd2VhBtUh%2FmIOqMxsrqBbpD3NQdWZjBdUi%2FWEOqs5srKBapD%2FMQdWZjRVUi%2FSHOag6s7GCapH%2BMAdVZzZWUC3SH%2Bag6szGCqpF%2BsMcVJ3ZWEG1SH%2BYg6ozGyuoFukPc1B1ZmMF1SL9YQ6qzmysoFqkP8xB1ZmNFVSL9Ic5qDqzsYJqkf4wB1VnNlZQLQezWVxcnJOTk5GRkZSU1Lp162ZN495TQsQi%2Fc3GCqplazbLysoyMzMTEhKmTJmSn59fWlpaWVnZSLu8DuE5qs5srKBajc9mUVFRXFxcdnZ2RUWFg3Z5HcJzVJ3ZWEG1GplNueuX6C8sLHTcLq9DeI6qMxsrqFYjs5mZmSl3%2FaG0y%2BsQnqPqzMYKqtXQbBYXFyckJDj6wOf%2Ft8vrEJ6j6szGCqrV0Gzm5ORMmTIlxHZ5HcJzVJ3ZWEG1GprN9PT0%2FPz8ENvldQjPUXVmYwXVamg2k5KSSktLQ2yX1yE8R9WZjRVUq6HZjImJqaqqCrFdXofwHFVnNlZQLbdeLbwO4T2qzmysoFqkP8xB1ZmNFVSL9Ic5qDqzsYJqkf4wB1VnNlZQLdIf5qDqzMYKqkX6wxxUndlYQbVIf5iDqjMbK6gW6Q9zUHVmYwXVIv1hDqrObKygWqQ%2FzEHVmY0VVIv0hzmoOrOxgmqR%2FjAHVWc2VlAt0h%2FmoOrMxgqqRfrDHFSd2VhBtUh%2FmIOqMxsrqBbpD3NQdWZjBdUi%2FWEOqs5srKBapD%2FMQdWZjRVUK2zpD7jBpXqGH7CCaoUn%2FWEfM6kKM6k7VlAt0t%2FvmElVmEndsYJqkf5%2Bx0yqwkzqjhVUi%2FT3O2ZSFWZSd6ygWqS%2F3zGTqjCTumMF1SL9%2FY6ZVIWZ1B0rqBbp73fMpCrMpO5YQbVIf79jJlVhJnXHCqpF%2BvsdM6kKM6k7VlAt0t%2FvmElVmEndsYJqkf5%2Bx0yqwkzqjhVUi%2FT3O2ZSFWZSd6ygWqS%2F3zGTqjCTumMF1SL9%2FY6ZVIWZ1B0rqBbp73fMpCrMpO5YQbVIf79jJlVhJnXHCqpF%2BvsdM6kKM6k7VlAt0t%2FvmElVmEndsYJqkf5%2Bx0yqwkzqjhVUi%2FT3O2ZSFWZSd6ygWqS%2F3zGTqjCTumMF1SL9%2FY6ZVIWZ1B0rqBbp73fMpCrMpO5YQbVIf79jJlVhJnXHCqpF%2BvsdM6kKM6k7VlAt0t%2FvmElVmEndsYJqkf5%2Bx0yqwkzqjhVUi%2FT3O2ZSFWZSd6ygWqS%2F3zGTqjCTumMF1SL9%2FY6ZVIWZ1B0rqBbp7zsHDx5sVg95KNyj0xg1qTtWUC3S348WLlw4bdq04D05OTkjRowI13jMQE3qjhVUi%2FT3o6qqqp49exYXF1s%2FVlZWJiQkFBUVhXdUuqMmdccKqkX6%2B9S6devS09PlMiDbeXl5KSkp1jZCRk3qjhVUi%2FT3r8zMzIKCAtlITU197bXXwj0c7VGTumMF1SL9%2FWvHjh3JyclFRUXx8fHHjx8P93C0R03qjhVUi%2FT3tenTp8tMZmdnh3sgJqAmdccKqkX6%2B1p5ebnMZFlZWbgHYgJqUnesoFqkv9998cUX4R6CIahJ3bGCaoUn%2Fev710xAE7lUz%2FADVlCtsKV%2FyC0D9aHqzMYKqkX6wxxUndlYQbVIf5iDqjMbK6gW6Q9zUHVmYwXVIv1hDqrObKygWqS%2Fv8yfP9%2F66w4hHxDJqDqzsYJqkf7%2B0rx587FjxzblgEhG1ZmNFVSL9A%2Bb1atXDxs2LDExccKECXv27JE9kyZNioqKSkhImDt3rvy4ZMmSPn36dO3addy4cZs3b659QGFhYb9%2B%2FTp27DhmzJgjR46E9%2Bn4AVVnNlZQLdI%2FPA4dOtSyZcuRI0fm5OS0b9%2F%2B%2Fvvvl525ubkS7j169Fi6dOmuXbtklvr27Ttr1qzY2NiUlJQaB5SUlERHR3fv3n3ixImtW7ceMGBAuJ9T%2BFF1ZmMF1SL9w%2BPtt9%2BWSRg7duzHH3%2B8cePGtWvXWvsDH%2Bxs27Zt4cKF%2B%2Fbtk8tAUlKS5HuNA%2BbMmSMtbNq06ezZs1OnTpXtrVu3huvp%2BARVZzZWUC3SPzxOnz6dmppq%2FXGCLl26rFixwtofCPczZ85MmjQpPj5e9rRr165Vq1Y1DsjKyqrxRw7WrFkTrqfjE1Sd2VhBtUj%2F8Dh27Ni33377%2Fvvvz549%2B%2Bqrr27Tps2JEyeqg8I9NzdXZumFF144depUWlpa7fSfPHmyHLB%2B%2FfrSKyoqKsL4jPyAqjMbK6gW6R8eK1eulEl47rnn9uzZM3r06KuuukpSXva3aNFi4MCBu3fvfvzxx%2BWAjRs3vvfee1FRUS1btrRODBwgbxfkgAkTJmzYsGHIkCGdO3e2rh%2BRjKozGyuoFukfHufOnRs1alTbtm1lKiS4Fy1aZO2XO%2FrY2Njhw4dv3769U6dO8uhNN900dOhQ2fj666%2BDD5DtuXPnJiYmykNxcXFyOQnn8%2FEHqs5srKBapH84yTXA%2Bq5nsPPnz1uf4Vy8ePGbb76pfVbgAOuYXbt2STtuD1ULVJ3ZWEG1SH%2BYg6ozGyuoFukPc1B1ZmMF1SL9YQ6qzmysoFqkP8xB1ZmNFVSL9Ic5qDqzsYJqkf4wB1VnNlZQLdIf5qDqzMYKqhW29Afc4FI9ww9YQbW494c5qDqzsYJqkf4wB1VnNlZQLdIf5qDqzMYKqkX6wxxUndlYQbVIf5iDqjMbK6gW6Q9zUHVmYwXV0i%2F9XfuuYLNGx0y%2FHvTbFI0%2BI5f6hTdYQbW0TP%2BQz21Ky%2FTrTb%2FutUx26I4VVIv0t9sy%2FXrTr3stkx26YwXVIv3ttky%2F3vTrXstkh%2B5YQbVIf7st0683%2FbrXMtmhO1ZQLdLfbsv0602%2F7rVMduiOFVSL9LfbMv160697LZMdumMF1SL97bZMv970617LZIfuWEG1SH%2B7LdOvN%2F261zLZoTtWUC3S327L9OtNv%2B61THbojhVUi%2FS32zL9etOvey2THbpjBdUi%2Fe22TL%2Fe9Otey2SH7lhBtUh%2Fuy3Trzf9utcy2aE7VlAt0t9uy%2FTrTb%2FutUx26I4VVIv0t9sy%2FXrTr3stkx26YwXVIv3ttky%2F3vTrXstkh%2B5YQbVIf7st0683%2FbrXMtmhO1ZQLdLfbsv0602%2F7rVMduguLi7u%2BPHj4R6FOUh%2Fuy3Trzf9utcy6a%2B7pKSk0tLScI%2FCHKS%2F3Zbp15t%2B3WuZ9Nddenp6QUFBuEdhDtLfbsv0602%2F7rVM%2Butu9uzZv%2FnNb8I9CnOQ%2FnZbpl9v%2BnWvZdJfd3Lj37dv33CPwhykv92W6debft1rmfTXXWVlZXx8%2FI4dO8I9EEOQ%2FnZbpl9v%2BnWvZdLfALNnzx4xYkRVVVW4B2IC0t9uy%2FTrTb%2FutUz6G6CiomLQoEHjxo0rKysL91i0R%2FrbbZl%2BvenXvZZ1TP9mgDuqSX%2F7LdOvN%2F2617J7%2FbpHxzHD%2F0h%2FZy3Trzf9uteyjkmq45jhf6S%2Fs5bp15t%2B3WtZxyTVcczwP9LfWcv%2B7%2Ffs2bMlJSXl5eUe93v06NETJ07YPNhRy06R%2FoAdJqd%2Fy5Yt27Rp07Zt21atWqWmpi5ZsqTpLdvst8avVgoLCz3oV8J33LhxV1999S233CJPOTMz8%2Fvvv%2Feg3%2Ffff%2F%2BOO%2B5ITk6%2B5ppr0tLS3nnnnUZPsdlyaEh%2F81y8eLGiouLcuXPhHohRDE%2F%2F4uJia%2Ft%2F%2Fud%2FYmJi9u3b18SWbfa7bdu2Rg9T3u%2BwYcPGjx8v9%2F6yfebMmXvvvTcjI8Ptfi9cuPCDH%2FxgzZo1si0vzqVLl8bGxv79739vYr9NQfr7TWVlZe1vm9i5JQrYvHmznPLMM8%2B4N0hH5s%2Bfb8DfGoqU9Bc9e%2Fb8j%2F%2F4jya2bLPf7du3N3qY2n7l5dG6devgD3wOHjwoNSo3Ta72e%2BTIETnmyy%2B%2FtH68dOmSvMey8xEQ6W%2BfjmMOZqX%2FjTfeODfIzp077bcg72KlmD%2F55BP3BulI8%2BbNx44dG%2B5RNFVEpL8U35%2F%2B9CdZsEOHDjWxZZv9vvjii6uusHmP0MR%2B33zzzZSUFDsdqe1X3H777Z06dXrsscfeffddec%2BhpN%2BmIP39xkr%2FO%2B%2B8s8Z%2BuUlKS0tbvHjxqFGjbr755ieeeELuHubMmTNo0KDvvvtODpD3lLK9du1auVTIxrJly7Zs2SIbK1asyMrKWrRokRwjdx7SSLt27Xr16mV9ultns9ZhsnPEiBE%2F%2FOEP%2F%2FCHP7z11lvykhkwYEBJSYk1Hnk70q9fv44dO44ZM0Zua%2BpratKkSVFRUQkJCXIN83QeVTM8%2FQNvM2NjY23%2BXUAl6T9w4MAhV4wePdqDfl955RUpUzsdqe23%2BvKnTHl5efLalklu3769knluCtLfb6z0T0xM%2FNkVv%2FjFL2T%2F559%2FLvtbtGhx2223SZjK9kcffSRRKxvLly%2BXAyRnrZu2wCc%2FH3%2F8sWxImcXExMiRR48ele1WrVqNHz%2FeakFutups9sMPP5QNeX%2Bcnp4eHR0tp8THx%2F%2FoRz%2BSnfIilb7kGiD7u3fvPnHiRDlMrgr1jTA3N1fSv0ePHkuXLg3vxDaR4en%2FwQcfyDVcSqTRD0BstuzbT37k%2FqhNmzbBH7ifP3%2F%2Bd7%2F7nbzwXO03mFwGpEc5ZcOGDY0eTPrbp%2BOYg9X%2B3P%2Baa66pvpKt9913n2xv2rRJtuXmWrJeEl9yX3bKe8qhQ4dWB33ub6X%2Frbfeunv3btkvt%2F%2Fyo1wGZHvv3r2ynZGRUWezVvo%2F9dRTsnPatGmyLW9VZTs5OVl6kQ15zyE75fizZ89OnTpVtrdu3VpnU9V88mO%2Fde9bttT43F9Jy75Nfyl9uR%2BRq11gj7yNTU1NdbvfP%2F7xjzV6kR%2Ft3BOR%2FvbpOOZgVvpLjp%2B7Qm5Nqq%2Bk%2F%2Fz582W7tLRUtmfOnCnbd9xxh7xR2LZtm%2ByxPsypkf6BX%2F%2FOmDFDfgy8zOX2PCkpqc5mrfR%2F%2BeWXZefTTz8t29aL9JZbbrnuuutkIysrq8Ylas2aNfWNkPS327r3LVvCmP6ffvrp0SBN%2Fw6MnX4nT56clpZ2%2BPBh2d6%2Ff3%2BvXr2WLVvmdr%2FffPONvOcIfMtTXlHyTlxeM03stylIf7%2Bp73N%2FK1sXLFhQ%2FY%2FZ%2Bvrrr8v2I488EhMTY33%2BXiP9rRAXubm58mN%2Bfr5snzx5UkK5f%2F%2F%2BdTbbQPpff%2F311ZdfPrJz%2Ffr1pVdUVFTUN0LS327r3rdsCWP617iJ8CCFqy%2F%2Fe6tRo0ZFR0d36dKlRYsW2dnZ1h2W2%2F0uX778Bz%2F4Qffu3Xv37n3TTTdZL5Um9tsUpL%2FfBD73nxYkcGddO1vLy8sl96WGMzMzrRbqS%2F8tW7bIW94%2BffqsXr16%2FPjx8tCzzz4bWvqvWLFCdk6YMGHDhg1Dhgzp3LnziRMn6huhjG3gwIHWp0%2F6Mjn93WjZ%2F%2F3KNeD%2F%2Fu%2F%2FGv24X22%2FZ8%2Be%2Feqrr%2Bz8cwqnLYeA9PebOr%2Fv%2F6tf%2Faq%2BbBV33XWX%2FJiXl2f9WF%2F6V1%2F%2B6D9wvyX342fOnHGa%2FgkJCVZTc%2BfOlUuUPBQXF7dy5crq%2Bt%2BdyBuF2NjY4cOHezSD7iD9nbVMv970617LOiapjmP2klxdSkpKrM%2BImujixYu7du2y84%2BK5Y11RUVF03sMI9LfWcv0602%2F7rWsY5LqOGb4H%2BnvrGX69aZf91rWMUl1HDP8j%2FR31jL9etOvey3rmKQ6jhn%2BR%2Fo7a5l%2BvenXvZZ1TFIdxwz%2FI%2F2dtUy%2F3vTrXss6JqmOY4b%2Fkf7OWqZfb%2Fp1r2Udk1THMcP%2FSH9nLdOvN%2F2617KOSarjmOF%2FpL%2BzlunXm37da1nHJG0GuKOa9LffMv160697LbvXr3t0HDP8j%2FR31jL9etOvey3rmKQ6jhn%2BR%2Fo7a5l%2BvenXvZZ1TFIdxwz%2FU5z%2BxcXFOTk5GRkZSUlJrVu3tvOpU1PG7QZ%2FpmGk9eteyzomqY5jhv8pS%2F%2BysrLMzMyEhIQpU6bk5%2BeXlpZaf9iv6S0rP7cpLdOvN%2F2617KOSarjmOF%2FatK%2FqKgoLi4uOzu7xh%2B9I%2F3p128t65ikOo4Z%2Fqcg%2FeWuX6K%2FsLDQ6bmkP%2F1637KOSarjmOF%2FCtI%2FMzNT7vpDOJf0p1%2FvW9YxSXUcM%2FyvqelfXFyckJBQ3%2F%2FlgPSnX7%2B1rGOS6jhm%2BF9T0z8nJ2fKlCmhnUv606%2F3LeuYpDqOGf7X1PRPT0%2FPz88P7VzSn369b1nHJNVxzPC%2FpqZ%2FUlJSaWlpaOeS%2FvTrfcs6JqmOY4b%2FNTX9Y2JiqqqqQjuX9Kdf71vWMUl1HDP8r6np796jDTMvO%2BjXm5Z1TFIdxwz%2FI%2F2dtUy%2F3vTrXss6JqmOY4b%2Fkf7OWqZfb%2Fp1r2Udk1THMcP%2FSH9nLdOvN%2F2617KOSarjmOF%2FpL%2BzlunXm37da1nHJNVxzPA%2F0t9Zy%2FTrTb%2Futaxjkuo4Zvgf6e%2BsZfr1pl%2F3WtYxSXUcM%2FyP9HfWMv160697LeuYpDqOGf6ncfq7h37D3m9TNPqMXOrXPTqOGf7XTNP0B%2BpjXtXpOGb4H%2BkP05hXdTqOGf5H%2BsM05lWdjmOG%2F5H%2BMI15VafjmOF%2FpD9MY17V6Thm%2BB%2FpD9OYV3U6jhn%2BR%2FrDNOZVnY5jhv%2BR%2FjCNeVWn45jhf6Q%2FTGNe1ek4ZvhfmNMfcEPINelPOo4Z%2FtcsjOkP%2B5hJVXScSR3HDP8j%2FfXATKqi40zqOGb4H%2BmvB2ZSFR1nUscxw%2F9Ifz0wk6roOJM6jhn%2BR%2FrrgZlURceZ1HHM8D%2FSXw%2FMpCo6zqSOY4b%2Fkf56YCZV0XEmdRwz%2FI%2F01wMzqYqOM6njmOF%2FpL8emElVdJxJHccM%2FyP99cBMqqLjTOo4Zvgf6a8HZlIVHWdSxzHD%2F0h%2FPTCTqug4kzqOGf5H%2BuuBmVRFx5nUcczwP9JfD8ykKjrOpI5jhv%2BR%2FnpgJlXRcSZ1HDP8j%2FTXAzOpio4zqeOY4X%2Bkvx6YSVV0nEkdxwz%2FI%2F31wEyqouNM6jhm%2BB%2FprwdmUhUdZ1LHMcP%2FSH89MJOq6DiTOo4Z%2Fkf6%2B9TBgweb1UMeCvfoNKZjTeo4Zvgf6e9fCxcunDZtWvCenJycESNGhGs8ZtCxJnUcM%2FyP9Pevqqqqnj17FhcXWz9WVlYmJCQUFRWFd1S607EmdRwz%2FI%2F097V169alp6fLZUC28%2FLyUlJSrG2ETMea1HHM8D%2FS3%2B8yMzMLCgpkIzU19bXXXgv3cLSnY03qOGb4H%2Bnvdzt27EhOTi4qKoqPjz9%2B%2FHi4h6M9HWtSxzHD%2F0h%2FDUyfPl1mMjs7O9wDMYGONanjmOF%2FpL8GysvLZSbLysrCPRAT6FiTOo4Z%2Fkf66%2BGLL74I9xAMoWNN6jhm%2BF8407%2B%2Bf80ENFHINelPOo4Z%2FtcsvOnf4NiAUJhXdTqOGf5H%2BsM05lWdjmOG%2F5H%2BMI15VafjmOF%2FpD9MY17V6Thm%2BB%2Fpr4GLFy9WVFScO3cu3APRg3lVp%2BOY4X%2Bkvxe%2B%2FfZb67so%2F%2FzP%2F2ztmTdvnrXnk08%2Bqe%2Bs%2BfPnW3%2FjYfPmzXLkM88849FwNWde1ek4Zvgf6e8FK%2F1jY2N79uxp7fnpT38qPzac%2Fs2bNx87dqxsfP%2F993IlaOBIBDOv6nQcM%2FyP9PeClf5paWlRUVHHjh2TPddff738GEj%2FL7%2F8Un5s165dr169lixZInsmTZokByckJMydO3fnzp2DBg1atmzZnDlzZOO7776TA9asWSPba9eule3CwsJ%2B%2Ffp17NhxzJgxR44csTpdvXr1sGHDEhMTJ0yYsGfPnrA9ec%2BZV3U6jhn%2BR%2Fp7wUr%2FRx55pFWrVuvWrdu7d6%2F8KFFupf%2FRo0fbt28vD40fP17iXnYWFBTk5uZK%2Bvfo0WPp0qWBT34WL14sG8uXL6%2B%2BfHmQNweHDh0qKSmJjo7u3r37xIkTW7duPWDAAHlU9rds2XLkyJE5OTnS%2BP333x%2FuOfCOeVWn45jhf6S%2FF6z0nzVrltyty738W2%2B91blz55deeslK%2F0WLFsmGJLscaV0YMjIyqoM%2B%2BQmkv2S67JTcl52dOnUaOnSobFhXkU2bNp09e3bq1KmyvXXr1rfffls25PSPP%2F5448aN1luECGFe1ek4Zvgf6e%2BFQPrPnDnzzjvvlP%2BOGjUqkP4zZsyQjcD%2Fw0tu%2F5OSkqrrSn%2FZvuOOOxITE7dt2yZ7rM%2BIsrKymv2jNWvWnD59OjU11fqxS5cuK1asCN%2Bz95p5VafjmOF%2FpL8XAum%2FcuXK9u3bSy7Pnz8%2FkP65ubmykZ%2BfL0eePHlSQr9%2F%2F%2F7V9aT%2F66%2B%2Fbn2IFBMTY33EP3nyZNmzfv360isqKiqOHTsmnb7%2F%2FvuzZ8%2B%2B%2Buqr27Rpc%2BLEibDOgXfMqzodxwz%2FI%2F29EEh%2FiWbrfvyDDz4IpP%2BWLVuioqL69OmzevXq8ePHy85nn31WzmrRosXAgQN3794dnP7l5eWS%2B%2FJQZmam1bjc18ujEyZM2LBhw5AhQzp37ixBL5cZ2fncc8%2Ft2bNn9OjRV1111alTp8I5BR4yr%2Bp0HDP8j%2FT3QiD9ZVvuxKOjo%2BX2PJD%2BsnPRokUtW7a0Lgxyv3%2FmzJnqyzf1sbGxw4cPr%2FF9%2F7vuukt%2BzMvLC7Q%2Fd%2B7cxMRE2RkXFye5L3vOnTs3atSotm3byk65Hkj73j%2FrcDGv6nQcM%2FyP9PeLysrKkpKSwPc1LefPn5frhJ3TL168uGvXrhr%2FHlh%2BjKjvelrMqzodxwz%2FI%2F1hGvOqTscxw%2F9If5jGvKrTcczwP9IfpjGv6nQcM%2FyP9IdpzKs6HccM%2FyP9YRrzqk7HMcP%2FSH%2BYxryq03HM8D%2FSH6Yxr%2Bp0HDP8L8zpD7gh5Jr0Jx3HDP9rxr0%2FDGNe1ek4Zvgf6Q%2FTmFd1nr93QqSoJv1hEqouYpWXl8v6lpWVhXsgOiH9YQ6qLmJNnz5d1jc7OzvcA9EJ6Q9zUHWRaceOHcnJyUVFRfHx8Tb%2FNiKqSX%2BYhKqLTJmZmQUFBbLRt2%2Ff1157LdzD0Yb26e%2F2L0boN7z9OtLoU1DVEfxj3bp16enpVVVVsp2Xl5eSkhLuEWnDhPRX1ZSjlunXm34VNkX6m0dCv2fPnoH%2FM3ZlZWWHDh2KiorCOypdkP4htky%2F3vSrsCnS3zwLFy6cNm1a8J6cnJwRI0aEazx6If1DbJl%2BvelXYVOkv2EOHjxY32eJ8lC4R6cB0j%2FElunXm34VNkX6m431dYr0D7Fl%2BvWmX4VNkQ5mY32dIv1DbJl%2BvelXYVOkg9lYX6dI%2FxBbpl9v%2BlXYFOlgNtbXKdI%2FxJbp15t%2BFTZFOpiN9XWK9A%2BxZfr1pl%2BFTZEOZmN9nSL9Q2yZfr3pV2FTpIPZWF%2BnSP8QW6Zfb%2FpV2BTpYDbW1ynSP8SW6debfhU2RTqYjfV1ivQPsWX69aZfhU2RDmZjfZ0i%2FUNsmX696VdhU6SD2Vhfp0j%2FEFumX2%2F6VdgU6WA21tcp0j%2FElunXm34VNkU6mI31dYr0D7Fl%2BvWmX4VNkQ5mY32dIv1DbJl%2BvelXYVOkg9lYX6dI%2FxBbpl9v%2BlXYFOlgNtbXKdI%2FxJbp15t%2BFTZFOpiN9XWK9A%2BxZfr1pl%2BFTZEOZmN9nSL9Q2w58GjLli2b%2FaPCwsIQurt06ZKjfpXzZ78KmyIdzMb6OkX6h9hycPpv27at6d1duHDBUb%2FK%2BbNfhU2RDmZjfZ0i%2FUNsOTj9t2%2FfXuPRr776aty4cc8%2F%2F3z37t3vueeeLVu2%2FPjHP77hhhtefPFF64D33nuvd%2B%2FeV1111ejRo7%2F%2F%2FnvZM3LkSEf9KufPfhU2RTqYjfV1ivQPseXg9JdMX3VFQUGB7Pz000%2BbN28%2Ba9asTZs2WSm%2FevXqtWvXRkVFnTp16ttvv42Pj%2F%2Fv%2F%2F7v8vLyhx9%2B%2BO6775ZT9u7d66hf5fzZr8KmSAezsb5Okf4hthyc%2FgMHDhxyhdzLV19Of8n3ixcvyvaMGTN%2B8pOfWAd37tx58%2BbNzz33nJX4Qm78pamTJ0%2FyyY%2FbTZEOZmN9nSL9Q2y54U9%2BJP179eplbWdnZz%2F11FPWdteuXf%2F6179OmTLlX%2F%2F1XwMHt23bds%2BePaS%2F202RDmZjfZ0yM%2F3PnTvnUsu1H60v%2FVNSUqzt2um%2FcOHCyZMnW3sOHDhw3XXXXbp0Scf092CeFTZFOpiN9XXKwPT%2F5ptvEhMTa%2ByUOL755pub2HKdj0r6S%2BNHg%2Fz9739vOP1LS0tvuOEGGafs%2Bfd%2F%2F%2Ff7779fNqyPiez3q5zTfuuc5wbUtwSkP5RgfZ2KlPSXRN6%2Ff38TW67z0drf91%2B2bFnD6S8bDz30kJw4aNCgpKSkLVu2hNCvHYHA3bx5c3JycvCG05abnv71LQHpDyVYX6fMSf%2FCwsJbb701NTV14cKFVirt2LFDbqtfeeWV22%2B%2FvaSkZPz48bJzwoQJ%2Bfn51imy8cgjj1Rf%2FoLmj3%2F8Yzlr0qRJJ0%2Be9OYpfPfdd9u3bw984GOzZUf9BgJXYfrXnufqeibw5Zdf7t27t1zeFixYID8GliCwLiE8o4aR%2FpGM9XXKkPQ%2FevRoXFycpMyHH37Yt29fK5Uk6WTnkCFD3nnnncBd8DPPPDNq1Cjr3J%2F85CeLFy%2BWtOrQocNLL71UVlb2L%2F%2FyLz%2F96U89fgqOWq796AMPPPDuu%2B%2FKxurVq9PS0qxPkGbOnFlUVBQIXFXpX%2Bc81zmBEvHy7ufAgQMfffSRHLZr167gNyLWutjp1xHSP5Kxvk4Zkv5yFx9IkzfffDOQ%2FjExMUeOHKkO%2Bgxk586dEj1yU3zmzJl27dodPnx46dKlAwYMsM4tLy9v0aKFx0%2FBUcu1H33sscd%2B%2FvOfy4YkrzxfSXzZvvbaaw8ePKj8k58657n2BJ4%2Bffr999%2B%2F7rrriouLZae8%2F5ArRPBgAuvSaL%2BOkP6RjPV1ypD0%2F9nPfpaTk2PtkdvMQPoHki74V45y07pu3Tq5Ux42bJj8%2BMQTT7Rp0%2BbaK5o3b%2B7xU3DUcu1H5bn86Ec%2Fkg251%2F6nf%2Fqn3%2F3ud9u3b7d%2B66A8%2Feuc59oTuG%2FfvurLv%2FCQ6%2BuNN94o77fkHUntwdjp1xHSP5Kxvk4Zkv4vvvjipEmTrD1r165tOP3nzZv36KOPTpw48Q9%2F%2BIP8%2BPzzzwc%2B7RF%2F%2B9vf7D%2BFhr9KdOnSpVdeeeX8%2BfMhPCP7j1pvYr7%2F%2FvvevXv%2F%2Bc9%2FfuCBB15%2F%2FfXZs2dXu5D%2Bdc5z7QmUJ37ixAkZmLzHeuedd6THt99%2Bm%2FSHq1hfpwxJf%2Bs%2BdM%2BePVVVVRLrDaf%2F119%2FfcMNN3Ts2NH6%2FeRnn3121VVXWberK1eu7NGjh%2F2n0PBXiS5cuCCHVVRUhPCMHD2anp4%2Bd%2B7cqVOnHj58WJ7yhAkT%2FvKXv1S7kP51znOdE7h06dIHH3zQ%2BiXEuHHjrO9Bkf5wD%2BvrlCHpX335yzwtWrTo2rXrI4880nD6i%2F79%2B1t%2FksHy9NNPt2zZslevXnJV%2BOijj%2Bw%2FhcCvVb%2F66isJu%2Fnz53fr1q1fv37WlzhHjhwph%2FXt21fugmt%2FK6bOU2z2W8Ozzz7boUOH5cuXy7Y8i4SEhNOnT1e7843P2vNcXdcEyr3%2FjTfeKIcNGjRInriMh%2FSHq1hfp8xJf1FWVmb9vcwQlJeXS%2F4G%2F%2BNVR5%2F8yEZMTMzMmTPl7njUqFEjRoyovvyH2%2BQwSXmJwtrfiqnzFJv91vDXv%2F5V9lt%2FJ%2B7hhx%2BWtLX2u%2FR9%2FzrnufYEyrZcHRtdEdIfSrC%2BThmV%2Fmo5Tf9rrrnG%2BvL%2Bxo0brU8%2FAp%2F81PmtmDpPsdmvG%2FzZr8KmSAezsb5Okf4htlw7%2FX%2F4wx9aD8nNvvWRSCD96%2FxWTJ2n2OzXDf7sV2FTpIPZWF%2BnSP8QW66d%2FoE%2F7VA7%2Fev8Vkydp9js1w3%2B7FdhU6SD2Vhfp0j%2FEFu2k%2F4XL16Mjo4%2BdOhQnd%2BKIf2Vt0z6RzLW1ynSP8SW7aS%2FuPvuuzt27Hj69Ona34oh%2FZW3TPpHMtbXKdI%2FxJbt93v8%2BHFro%2Fa3Ylzt1yl%2F9quwKdLBbKyvU6R%2FiC3Trzf9KmyKdDAb6%2BsU6R9iy%2FTrTb8KmyIdzMb6OkX6h9gy%2FXrTr8KmSAezsb5Okf4htky%2F3vSrsCnSwWysr1Okf4gt0683%2FSpsinQwG%2BvrFOkfYsv0602%2FCpsiHczG%2BjpF%2BofYMv1606%2FCpkgHs7G%2BTpH%2BIbZMv970q7Ap0sFsrK9TpH%2BILdOvN%2F0qbIp0MBvr6xTpH2LL9OtNvwqbIh3Mxvo6RfqH2DL9etOvwqZIB7Oxvk6R%2FiG2TL%2Fe9KuwKdLBbKyvU6R%2FiC3Trzf9KmyKdDAb6%2BsU6R9iy%2FTrTb8KmyIdzMb6OkX6h9gy%2FXrTr8KmSAezsb5Okf4htky%2F3vSrsCnSwWysr1Okf4gt0683%2FSpsinQwG%2BvrFOkfYsv0602%2FCpsiHczG%2BjpF%2BofYMv1606%2FCpkgHs7G%2BTpH%2BIbZMv970q7Ap0sFsrK9TpH%2BILdOvN%2F0qbIp0MBvr6xTpH2LL9OtNvwqbIh3Mxvo6RfqH2DL9etOvwqZIB7Oxvk6R%2FiG2TL%2Fe9KuwKdLBbKyvU6R%2FiC3Trzf9KmyKdDAb6%2BuUCenvHvoNe7%2BONPoUVHUEH2J9ndI%2B%2FYEAqi6Ssb5Okf4wB1UXyVhfp0h%2FmIOqi2Ssr1OkP8xB1UUy1tcp0h%2FmoOoiGevrFOkPc1B1kYz1dYr0hzmoukjG%2BjpF%2BsMcVF0kY32dIv1hDqoukrG%2BToUt%2FQE3hFyT0B3r6xR3937HTKrCTJqN9XWK9Pc7ZlIVZtJsrK9TpL%2FfMZOqMJNmY32dIv39jplUhZk0G%2BvrFOnvd8ykKsyk2Vhfp0h%2Fv2MmVWEmzcb6OkX6%2Bx0zqQozaTbW1ynS3%2B%2BYSVWYSbOxvk6R%2Fn7HTKrCTJqN9XWK9Pc7ZlIVZtJsrK9TpL%2FfMZOqMJNmY32dIv39jplUhZk0G%2BvrFOnvd8ykKsyk2Vhfp0h%2Fv2MmVWEmzcb6OkX6%2Bx0zqQozaTbW1ynS3%2B%2BYSVWYSbOxvk6R%2Fn7HTKrCTJqN9XWK9Pc7ZlIVZtJsrK9TpL%2FfMZOqMJNmY32dIv39jplUhZk0G%2BvrFOnvOwcPHmxWD3ko3KPTGDVpNtbXKdLfjxYuXDht2rTgPTk5OSNGjAjXeMxATZqN9XWK9Pejqqqqnj17FhcXWz9WVlYmJCQUFRWFd1S6oybNxvo6Rfr71Lp169LT0%2BUyINt5eXkpKSnWNkJGTZqN9XWK9PevzMzMgoIC2UhNTX3ttdfCPRztUZNmY32dIv39a8eOHcnJyUVFRfHx8cePHw%2F3cLRHTZqN9XWK9Pe16dOny0xmZ2eHeyAmoCbNxvo6Rfr7Wnl5ucxkWVlZuAdiAmrSbKyvU6S%2F333xxRfhHoIhqEmzsb5OhSf96%2FvXTEAThVyT0B3r61TY0r%2FhYQEhoOoiGevrFOkPc1B1kYz1dYr0hzmoukjG%2BjpF%2BsMcVF0kY32dIv395dChQxs2bLh48aIHfUkvFRUV586d86Avb1B1kYz1dYr0V2b%2B%2FPnWH2ZoylmPPvpocnJydHT073%2F%2F%2B0bP%2Fd%2F%2F%2Fd9WrVq9%2FPLLTju1bN68WRbimWeeCe10H4rAqkMA6%2BsU6a9M8%2BbNx44d25Szzp8%2F36FDh6lTp8r8vPHGG42e%2B%2Fnnn8uRCxYscDzWy77%2F%2Fnu59nzyySehne5DEVh1CGB9nSL9G%2FHll1%2BmpaW1a9euV69eS5Ysqb6cuYMGDVq1apVs79q1S7aXLVs2adKkqKiohISEuXPnWgfIwaNHj5Ybedlz4cKFRs%2BS%2FWvWrJGLgRwj8%2FP000%2Ffeeed3bt3%2F%2BUvf2l9EFRYWNivX7%2BOHTuOGTPmyJEj1VfSPzs7e%2BjQoZ06dfrZz35mfYwjXffp06dr167jxo2TG3zZM2fOHOnxu%2B%2B%2Bs3qR7bVr1%2B7cudMaRp1Pc8uWLfLoihUrsrKyFi1aFKbpd8aYqkMIWF%2BnSP%2BGHD16tH379q1atRo%2FfrxktAy7oKCgqKhINqzPWyQ0ZfvXv%2F51bm6u5HiPHj2WLl1qHSBnSSi3bdtWthcvXtzoWbJf3gRkZma%2B%2B%2B678mhMTIykf5cuXWQ7Pz%2B%2FpKQkOjpaLgYTJ05s3br1gAEDqq%2Bkv1wwRo4caR0p7xjk0iIbffv2nTVrVmxsbEpKihwpA5Cdy5cvl2255Mgphw4dCnzyU%2BfT%2FPjjj2VD9stI5PRwLoNtZlQdQsP6OkX6N0Tuea3slu29e%2FfKdkZGRp05Xh30GY51wMMPPyzbpaWlsj148OBGzzpx4oTE%2BsqVK630f%2BSRR2Tn%2Fv37JfQfeughuXmXnZs2bTp79qz10dDWrVut9L%2FnnnvkSCusn3rqqW3bti1cuHDfvn1yGUhKSpI2qy%2F%2FMlk6ktyXbXmXIJel6qDP%2Fet8mlaDt9566%2B7du8Mw9SExo%2BoQGtbXKdK%2FITNmzJChBv4fW3JfLHkanONWgNaZ%2Fnl5edZZclferVu3Rs9atmxZXFzcmTNnrPQP%2FNZXTu%2FZs2dWVlazf7RmzRor%2FefNmyeH7dy5U7Yfe%2BwxaUFSPj4%2BXlpu166d3NFb7dxxxx2JiYlybZDDrM92Aulf59O00l%2Bv3wmbUXUIDevrFOnfkNzcXOuDF9k%2BefKk5Gn%2F%2Fv2tWHz%2B%2Bedlp5XUdaa%2FRKpsSxbHxMTYOev222%2B37s2tR3%2F5y19ap7do0eLee%2B%2BdPHmy7Fy%2Ffn3pFRUVFcG%2F9Q2kvzXmF1544dSpU2lpaYH0f%2F311623FDIe69cGgfRv4GmG%2FIWisDCj6hAa1tcp0r8hW7ZsiYqK6tOnz%2BrVq8ePHy%2FDfvbZZw8dOiQbcj8uedq9e%2FdAjktMDxw4cPfu3Vb6X3vttatWrZLglu0nnnii4bM%2B%2FPBD6cj6P%2Fda6X%2F99dfLkRMnTrRu1VesWCEbEyZM2LBhw5AhQzp37nzixIk60%2F%2Fxxx%2BXjY0bN7733nvSZsuWLa3nUl5eLrkv3WVmZlp7Aulf59Mk%2FaEX1tcp0r8RixYtkgC1PmyRm3S5GZedcicucSn3yNYtuZXjsh0bGzt8%2BHAr%2Fe%2B%2B%2B%2B527drJhtyAHz16tOGzbr75Zgl067s9VvqPGDFC9lunW%2F9jr7lz5yYmJsqeuLi4lStXVv%2FjNz4D6b99%2B%2FZOnTrJ9k033TR06FDZ%2BPrrr63nctdddwV%2FJBX8ff%2FaT5P0h15YX6dI%2F8ZVVlaWlJRYn5YEnDx58tSpUzWOPH%2F%2BfEVFhZX%2Bb7zxxrlz5w4cOGDzrNr9yunWdzQD5PKwa9euRv91rhz2zTffNHxMbXU%2BTb2YVHVwivV1ivRXL5D%2B4R5IxInkqgPr6xTpr96ePXtycnKsf2YFL0Vy1YH1dYr0hzmoukjG%2BjpF%2BsMcVF0kY32dalKCV1VVhXxuoyMDnKLqIhnr61Tor5aEhITS0tLQzmWd4AaqLpKxvk6F%2FmrJyMhYt25daOeyTnADVRfJWF%2BnQn%2B1ZGdnz549u4FzDx482MCjgBtCrmfojvV1KvRXyxdffJGUlFTfR%2F%2FWS3HEiBEffPCB05aB0JD%2BkYz1dapJr5asrKz6%2FsdSVvo%2F%2BeSTHTp0SElJWbp0afA%2FZ2Wd4AbSP5Kxvk416dVSVlYmt%2F%2B%2F%2Bc1v9u%2FfX%2Ftc6%2FTKysq8vLzU1NT4%2BPjs7Gw5xU7LQAhI%2F0jG%2BjrV1FfL8ePHp02bJsne6Iew1t8%2FEJ999hnrBDeQ%2FpGM9XXKrVdLjfRft25dSkqKXCesXwWzTnAD6R%2FJWF%2BnXE%2F%2FHTt2ZGZmpqenB%2F7XUU1sGagP6R%2FJWF%2Bn3E3%2F6dOnJycnFxQU1PhqEOsEN5D%2BkYz1dcrd9J83b16df7medYIbSP9Ixvo65WL61%2F4ikJKWgfqQ%2FpGM9XXKxfR3qWWgPlRdJGN9nSL9YQ6qLpKxvk6R%2FjAHVRfJWF%2BnSH%2BYg6qLZKyvU6Q%2FzEHVRTLW1ynSH%2Bag6iIZ6%2BsU6Q9zUHWRjPV1ivSHOai6SMb6OkX6wxxUXSRjfZ0i%2FWEOqi5i7d%2B%2FPzExMdyj0AzpD3NQdRGrsLBw8ODB4R6FZkh%2FmIOqi1izZ8%2FOzs4O9yg0Q%2FrDHFRdZKqqqkpOTv7ss8%2FCPRDNNPR6iImJqfF3%2BR20y%2BsQnqPqItOTTz6ZkZER7lHop6HXQ1JSUmlpaYjt8jqE56i6CFRUVBQfHx9yUkWyhl4P6enp%2Bfn5IbbL6xCeo%2BoiSlVVldz1S%2FQXFhaGeyxaauj1kJOTM2XKlBDb5XUIz1F1keDgwYMS97Nnz05OTs7IyOCuP2QNvR6Ki4sTEhLq%2FF8zNt4u%2F28veE7qqqSkRO4Hs7KyevbsGRcX1wzGkVAaPHhwdnY2v%2BZtokZSODMzM7TvUVnrtGDBgsrKyjofDaFNoGFSVx06dJCKXbVqlVwGjh8%2FXuPRcA0M8KFGXg9lZWVyAxXCx2pW%2Bk%2BbNk3endU%2Bndch3CB11cBbVaoOCNb466GoqEguAHI%2FVeddfL3tXiYbO3bsyMjISE9Pl3ux4EdDGCvQsIbriqoDgtl6Pcg7gMzMzISEhClTpuTn55eWljb67wAC6W%2BR2%2F%2BePXvKW4Hy8vJqXodwB%2BkP2Ofg9VBcXJyTkyN38UlJSTExMXZ%2BPxN8%2BgcffGDt%2FOyzz3gdwg2kP2CfW6%2BHQPpXVlauWLFi0KBB8fHx2dnZ1tezeB3CDaQ%2FYJ%2B76S%2FvFTp06JCSkvLqq68GfwGD1yHcQPoD9rmb%2FpmZmUVFRbV%2FScDrEG4g%2FQH7XEz%2FgwcPNvCoS%2F0ikpH%2BgH0upn%2FIjwKhoeoA%2B0h%2FmIOqA%2Bwj%2FWEOqg6wj%2FSHOag6wD7SH%2Bag6gD7SH%2BYg6oD7CP9YQ6qDrCP9Ic5qDrAPtIf5qDqAPtIf5iDqgPsI%2F1hDqoOsI%2F0hzmoOsA%2B0h%2FmoOoA%2B0h%2FmIOqA%2Bwj%2FWEOqg6wj%2FSHOag6wD7SH%2Bag6gD7SH%2BYg6oD7CP9YQ6qDrCP9Ic5qDrAPtIf5qDqAPtIf5iDqgPsI%2F1hDqoOsI%2F0hzmoOsA%2B0h%2FmoOoA%2B0h%2FmIOqA%2Bwj%2FWEOqg6wj%2FSHOag6wD7SH%2Bag6gD7SH%2BYg6oD7CP9YQ6qDrCP9Ic5qDrAPtIf5qDqAPtIf5iDqgPsI%2F1hDqoOsI%2F0hzmoOsA%2B0h%2FmoOoA%2B0h%2FmIOqA%2Bwj%2FWEOqg6wj%2FSHOag6wD7SH%2Bag6gD7SH%2BYg6oD7CP9YQ6qDrCP9Ic5qDrAPtIf5qDqAPtIf5iDqgPsI%2F1hDqoOsI%2F0hzmoOsA%2B0h%2FmoOoA%2B0h%2FmIOqA%2BwLW%2FoDbgi5JoFIE570B7xHTQLBSH9ECmoSCEb6I1JQk0Aw0h%2BRgpoEgpH%2BiBTUJBCM9EekoCaBYKQ%2FIgU1CQQj%2FREpqEkgGOmPSEFNAsFcTH%2FAb1yqdkBHvB4AIBL9P%2B4rh6KEQdQNAAAAAElFTkSuQmCC%22%2B%2F
		String myUrl = "http://testsubdomain.socialvinesolutions.com:9001/?uid="+userData.USERID+"&data="+imgData;
		Log.d(TAG,"connecting to "+myUrl);
		URL url;
		try {
			url = new URL(myUrl);
		} catch (MalformedURLException e) {
			Log.e(TAG,"ERR: malformed URL");
			e.printStackTrace();
			return;
		}
		
		//this code throws networkOnMainThread exception
	/*	try {
			URL url = new URL(myUrl);
			HttpURLConnection urlConnection;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
				try {
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					//readStream(in);
				} finally {
					Log.d(TAG,"disconnecting from server");
				     urlConnection.disconnect();
				}
			} catch (IOException e) {
				Log.e(TAG,"IOexception when connecting");
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			Log.e(TAG,"ERR: malformed URL");
			e.printStackTrace();
		}
*/
		
/*		this code throws networkOnMainThread exception
		try{
			String URL = "http://testsubdomain.socialvinesolutions.com:9001/?uid="+userData.USERID+"&data="+imgData;
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(URL));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        //..more logic
			} else{
			    //Closes the connection.
			    response.getEntity().getContent().close();
			    throw new IOException(statusLine.getReasonPhrase());
			}
		} catch(Exception e){
		    Log.e(TAG,"ERR sending image");
		    e.printStackTrace();
		}
		*/
		
		// this code executes fine and does not show problems, but nothing gets to the server
		class RequestTask extends AsyncTask<String, String, String>{
		    @Override
		    protected String doInBackground(String... uri) {
		        HttpClient httpclient = new DefaultHttpClient();
		        httpclient.getParams().setParameter("http.socket.timeout", 5000);
		        HttpResponse response;
		        String responseString = null;
		        try {
		            response = httpclient.execute(new HttpGet(uri[0]));
		            StatusLine statusLine = response.getStatusLine();
		            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		                ByteArrayOutputStream out = new ByteArrayOutputStream();
		                response.getEntity().writeTo(out);
		                out.close();
		                responseString = out.toString();
		            } else{
		                //Closes the connection.
		                response.getEntity().getContent().close();
		                throw new IOException(statusLine.getReasonPhrase());
		            }
		        } catch (ClientProtocolException e) {
		        	Log.e(TAG,"image send err: clientProtocolException:"+e.getMessage());
		        	e.printStackTrace();
		            //TODO Handle problems..
		        } catch (IOException e) {
		        	Log.e(TAG,"image send err: ioException:"+e.getMessage());
		        	e.printStackTrace();
		            //TODO Handle problems..
		        }
		        Log.d(TAG,"responseString="+responseString);
		        return responseString;
		    }
		    @Override
		    protected void onPostExecute(String result) {
		        super.onPostExecute(result);
		        Log.d(TAG,"response="+result);
		        //Do anything with response..
		    }
		}		
		RequestTask my_task = new RequestTask();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		    my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myUrl);
		else
		    my_task.execute(myUrl);
		
		Log.d(TAG,"upload complete?");
	}
}
