package simgrideclipseplugin.wizards;

import java.util.ArrayList;

import org.eclipse.cdt.core.settings.model.CExternalSetting;
import org.eclipse.cdt.core.settings.model.CLibraryPathEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLibraryPathEntry;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.extension.CExternalSettingProvider;
import org.eclipse.core.resources.IProject;

public class SimgridCExtenalSettings extends CExternalSettingProvider {

	/** The ID of this ExternalSettingProvider as specified in the plugin XML */
	public static final String ID = "simgrideclipse.external_settings"; //$NON-NLS-1$
	public static String lib_path;

	/** {@inheritDoc} */
	@Override
	public CExternalSetting[] getSettings(final IProject pr_Project,
			final ICConfigurationDescription pr_Config) {
		if (lib_path != null){
		// supply a simple macro entry
		ArrayList<ICSettingEntry> pathEntries = new ArrayList<ICSettingEntry>();
		ICLibraryPathEntry include = new CLibraryPathEntry(lib_path, ICSettingEntry.ALL);
		pathEntries.add(include);

		// ICMacroEntry macro = new CMacroEntry("MY_DEFINE", "\"my_value\"", 0);
		// pathEntries.add(macro);

		ICSettingEntry[] settings = pathEntries
				.toArray(new ICSettingEntry[pathEntries.size()]);
		return new CExternalSetting[] { new CExternalSetting(null, null, null,
				settings) };
		}
		return  new CExternalSetting[0];
	}
}
