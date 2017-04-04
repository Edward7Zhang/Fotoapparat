package io.fotoapparat.hardware.v2.capabilities;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Size;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Provides infromation about the possible sizes the camera can take pictures of.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SizeCapability {

	private final Characteristics characteristics;

	public SizeCapability(Characteristics characteristics) {
		this.characteristics = characteristics;
	}

	/**
	 * @return the largest size the camera can take a picture of
	 */
	@SuppressWarnings("ConstantConditions")
	public Size getLargestSize() {
		StreamConfigurationMap configurationMap = characteristics
				.getCameraCharacteristics()
				.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

		return Collections.max(
				Arrays.asList(configurationMap.getOutputSizes(ImageFormat.JPEG)),
				new CompareSizesByArea()
		);
	}

	/**
	 * Comparator based on area of the given {@link Size} objects.
	 */
	private static class CompareSizesByArea implements Comparator<Size> {

		@Override
		public int compare(Size lhs, Size rhs) {
			// We cast here to ensure the multiplications won't overflow
			return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
					(long) rhs.getWidth() * rhs.getHeight());
		}
	}
}
