
package com.example.biggly

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biggly.databinding.FragmentSecondBinding
import com.example.biggly.retrofit.APIClient
import com.example.biggly.retrofit.APIInterface
import com.example.biggly.retrofit.ResponseModel
import com.example.biggly.service.SoundService
import com.example.biggly.service.SoundService.MyBinder
import com.example.biggly.utility.ImageChosser
import com.example.biggly.utility.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
 * A simple [Fragment] subclass as the second destination in the navigation.*/

class SecondFragment : Fragment() , PRRequestBody.FileUploadercallback{
    private var mServiceBound: Boolean =false
    private val permissions: ArrayList<String?> = ArrayList<String?>()
    private  var mCurentPercent = 0;
    private var  isRunning : Boolean = false

    private var requestFile1 :PRRequestBody? =null

    private var mHeroImagebody: MultipartBody.Part? = null
    private var _binding: FragmentSecondBinding? = null

    private var imageChosser: ImageChosser? = null

    private var mSaveBit :File? = null
    private var filesAdapter :FilesAdapter? = null
    private var surveyImagesParts : Array<MultipartBody.Part?>? = null
    private var surveyImagesParts1 : ArrayList<MultipartBody.Part?>? = null
    private var pathList : ArrayList<String?>? = null
    private var filepathList : ArrayList<String?>? = null


    private var isUploaded : Boolean =false

    private val binding get() = _binding!!
     var fileList : ArrayList<FileModel>? = null

    var isUploading:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.getWindow()!!.setBackgroundDrawableResource(R.drawable.main_app_bg);

        imageChosser = ImageChosser()
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        surveyImagesParts1 = ArrayList()
        pathList = ArrayList()
        filepathList = ArrayList()
        fileList = ArrayList();

        binding.filesRecycler.setLayoutManager(LinearLayoutManager(activity))
        binding.filesRecycler.setHasFixedSize(true)

        binding.filesRecyclerLarge.setLayoutManager(LinearLayoutManager(activity))
        binding.filesRecyclerLarge.setHasFixedSize(true)

        setfilesAdapter()
//        binding.frameView1.setOnClickListener{
//            selectFile()
//        }
        return binding.root

    }
    fun slideToRight(view: View) {
        view.visibility= View.GONE
        val slideAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        view.startAnimation(slideAnimation)
//        val animate = TranslateAnimation(0f, view.width.toFloat(), 0f, 0f)
//        animate.duration = 500
//        animate.fillAfter = true
      //  view.startAnimation(animate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {

    if(pathList!!.size>0) {
    if (Validator.isValidateMultipleEmail(
            activity,
            binding.email,
            resources.getString(R.string.please_enter_valid_email)

        )
        && Validator.isValidateEmail(
            activity,
            binding.emailFrom,
            resources.getString(R.string.please_enter_valid_email)

        )
        && Validator.isTermsSelected(
            activity,
            binding.terms,
            resources.getString(R.string.please_accept_terms)
        )
    ) {
        if (Utility.isNetworkAvailable(activity)) {
            val intent1 = Intent(activity, SoundService::class.java)
            intent1.action = SoundService.ACTION.START_ACTION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //activity?.bindService(intent1,mServiceConnection, Context.BIND_AUTO_CREATE)
                activity?.startForegroundService(intent1)
            } else {
                activity?.startService(intent1)
            }
            isRunning = true
            Utility.hideKeyboard(activity)

            Utility.getInstance(activity).showLoading(resources.getString(R.string.uploading_data))
            callCompleteProfile()
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.please_check_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
        }else{
            Toast.makeText(
                activity,
                resources.getString(R.string.add_file),
                Toast.LENGTH_SHORT
            ).show()
        }

        }


        binding.toggle.setOnClickListener {
            slideToLeft(binding.showLayout)
        }
        binding.cancel.setOnClickListener {
            slideToRight(binding.showLayout)
        }

        binding.show.setOnClickListener {

            startActivity(Intent(requireActivity(),LaunchScreens::class.java))
            requireActivity().finish()
        }
        binding.childScroller.setOnTouchListener(OnTouchListener { view, motionEvent ->
            binding.mainScroller.requestDisallowInterceptTouchEvent(true)
            false
        })
        binding.frameView.setOnClickListener{
            selectFile()
        }

        binding.parent.setOnClickListener{
            selectFile()
        }
        binding.clickText.setOnClickListener{
           selectFile()
        }
        binding.clickText1.setOnClickListener{
            selectFile()
        }

        binding.maximize.setOnClickListener{
            binding.detailedView.visibility = View.INVISIBLE
            binding.frameView.visibility = View.VISIBLE
        }

        binding.minimize.setOnClickListener{
            binding.detailedView.visibility = View.VISIBLE
            binding.frameView.visibility = View.GONE
            if(fileList!!.size==0){
                binding.childScroller.visibility = View.GONE
                binding.selectFile.visibility = View.VISIBLE
            }else{
                binding.childScroller.visibility = View.VISIBLE
                binding.selectFile.visibility = View.GONE
            }
        }
    }

    private fun setfilesAdapter() {

        if (filesAdapter == null) {

            filesAdapter = FilesAdapter(context, fileList)
            binding.filesRecycler.setAdapter(filesAdapter)
            binding.filesRecyclerLarge.setAdapter(filesAdapter)

        } else {

            filesAdapter?.notifyDataSetChanged()

        }
        if(pathList?.size ==0){
            binding.childScroller.visibility = View.GONE
            binding.selectFile.visibility = View.VISIBLE

        }
    }

    private fun selectFile() {

        permissions.add(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

            permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)

        }else{

            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }

        (activity as BaseActivity?)?.setPermissionsToRequest(permissions)
        (activity as BaseActivity?)?.askPermission(object : BaseActivity.onPermissionResult {
            override fun onPermissionGranted() {

                startActivityForResult(imageChosser?.getPickFileChooserIntent(activity), 200)

            }

            override fun onPermissiondenied() {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {

                    if (data.getClipData() != null) {

                        var count: Int = data!!.getClipData()!!
                            .getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    while(count>0){
                        count = count-1
                            var returnUri :Uri= data!! . getClipData ()!!.getItemAt(count).getUri();
                            val path1: String = imageChosser!!.getPath1(activity, returnUri)
                            if (path1 == null) {

//                        path1 = getFilePath(getActivity(),data.getData());
//                        if(path1 == null)
//                        setImageafterCrop(data.getData());
//                        else
                                setImageafterCrop(returnUri)
                            } else if (path1.equals("nodata", ignoreCase = true)) {
                                setImageafterCrop(returnUri)
                            } else {
                                setImageafterCrop(path1)
                            }
                        }
                    }else{

                        var returnUri :Uri?= data?.data;
                        val path1: String = imageChosser!!.getPath1(activity, returnUri)
                        if (path1 == null) {

//                        path1 = getFilePath(getActivity(),data.getData());
//                        if(path1 == null)
//                        setImageafterCrop(data.getData());
//                        else
                            setImageafterCrop(returnUri)
                        } else if (path1.equals("nodata", ignoreCase = true)) {
                            setImageafterCrop(returnUri)
                        } else {
                            setImageafterCrop(path1)
                        }
                    }

                            //  picUri = getRealPathFromURI(data.getData());

                } catch (e : java.lang.Exception) {
                    e.printStackTrace()
                    setImageafterCrop(data.getData());
                }

            } else {
                imageChosser!!.index= imageChosser!!.index+1

                val path: String = imageChosser?.camProfilePic!!.getPath()
                Log.e("CAMERA_PATH", path)
                setImageafterCrop(path)

            }
        }
    }
    private fun setImageafterCrop(s: Uri?) {
        binding.childScroller.visibility = View.VISIBLE
        binding.selectFile.visibility = View.GONE
        pathList?.add(s?.path)
        mSaveBit = File(s?.path)



        var layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        var view =   layoutInflater.inflate(R.layout.file_layout,null)

        var textView =   view.findViewById(R.id.file_name) as TextView
        textView.setText(mSaveBit?.name)
      //  binding.filesLayout.addView(view)


        var fileModel  = FileModel()
        fileModel.file_name = mSaveBit?.name

        fileList?.add(fileModel)
        setfilesAdapter()

    }
    private fun setImageafterCrop(s: String) {
        binding.childScroller.visibility = View.VISIBLE
        binding.selectFile.visibility = View.GONE

        filepathList?.add(s)
        pathList?.add(s)
        mSaveBit = File(s)
       //requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mSaveBit!!)



        var layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        var view =   layoutInflater.inflate(R.layout.file_layout,null)

        var textView =   view.findViewById(R.id.file_name) as TextView
        var imageView =   view.findViewById(R.id.cross) as ImageView
        textView.setText(mSaveBit?.name)
       // binding.filesLayout.addView(view)
        var fileModel  = FileModel()
        fileModel.file_name = mSaveBit?.name

        fileList?.add(fileModel)
        setfilesAdapter()

    }
    fun createFileBody(s: String?){
        mSaveBit = File(s)
        requestFile1 = PRRequestBody(mSaveBit,pathList!!.size,this)

        mHeroImagebody =
            MultipartBody.Part.createFormData("file[]", mSaveBit!!.name, requestFile1!!)

        surveyImagesParts1?.add(mHeroImagebody)
    }

    fun createBody(s:String?,index: Int){

        mSaveBit = File(s)

        // requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSaveBit);
        val extension = s!!.substring(s!!.indexOf(".") + 1)
        //    Toast.makeText(activity, extension, Toast.LENGTH_SHORT).show()
        if (extension.equals("pdf", ignoreCase = true)) {
//            requestFile =
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mSaveBit!!.absolutePath)

            requestFile1 = PRRequestBody(mSaveBit,index,this)

        } else if (extension.equals("doc", ignoreCase = true) || extension.equals(
                "docs",
                ignoreCase = true
            ) || extension.equals("docx", ignoreCase = true)
        ) {
            //    requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), s.path!!)
            requestFile1 = PRRequestBody(mSaveBit,index,this)

        } else {
            // requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), s.path!!)
            requestFile1 = PRRequestBody(mSaveBit,index,this)

        }
        mHeroImagebody =
            MultipartBody.Part.createFormData("file[]", mSaveBit!!.name, requestFile1!!)

        surveyImagesParts1?.add(mHeroImagebody)
    }


    fun removeFIle(index: Int){
        pathList?.removeAt(index)


        if(pathList?.size ==0){
            binding.childScroller.visibility = View.GONE
            binding.selectFile.visibility = View.VISIBLE

        }
    }
     fun callCompleteProfile() {
        try {
            disableView();
            isUploading = true
            for(index in 0 until  pathList!!.size){

                this.createBody(pathList?.get(index),index)

            }


            surveyImagesParts  = arrayOfNulls<MultipartBody.Part>(
                surveyImagesParts1!!.size
            )
            for(index in 0 until  surveyImagesParts1!!.size){
                surveyImagesParts?.set(index, surveyImagesParts1?.get(index))
            }

            val utility: Utility = Utility.getInstance(context)
            val email_to: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(),binding.email.text.toString())
            val emailFrom: RequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.emailFrom.text.toString())
            val bot: RequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "1")

            val message: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(),binding.message.text.toString())



          //  utility.showLoading(getString(R.string.please_wait))
            val apiInterface: APIInterface =
                APIClient.getClient(context).create(APIInterface::class.java)
            var callApi: Call<ResponseModel?>? = null
            callApi =
                apiInterface.createEvent(
                    email_to,
                    emailFrom,
                    message,
                    bot,
                    binding.terms.isChecked,
                    surveyImagesParts
                )

            callApi.enqueue(object : Callback<ResponseModel?> {
                override fun onResponse(
                    call: Call<ResponseModel?>,
                    response: Response<ResponseModel?>
                ) {
                    utility.hideLoading()

                    try {

                        fileList!!.clear()
                        filepathList!!.clear()
                        pathList!!.clear()
                        setfilesAdapter()
                        System.out.println(response.body().toString()) //convert reponse to string

                        if((response.body()?.status).equals("success")) {
                            isUploaded=true
                            findNavController().navigate(R.id.action_SecondFragment_to_sucessFragment)
                        }else{
                            isUploaded=false
                            disableView();
                            Toast.makeText(
                                context,
                                response.body()?.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    isRunning = false;
                  stopService()
                }

                override fun onFailure(call: Call<ResponseModel?>, t: Throwable) {
               //   findNavController().navigate(R.id.action_SecondFragment_to_sucessFragment)

                    fileList!!.clear()
                    filepathList!!.clear()
                    pathList!!.clear()
                    setfilesAdapter()
                     Toast.makeText(
                        context,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    isUploaded=false
                  //  isUploading= true
//                    binding.emailFrom.setText("")
//                    binding.email.setText("")
//                    binding.message.setText("")
//                    binding.filesLayout.removeAllViews()
//                    binding.childScroller.visibility = View.VISIBLE
//                    binding.selectFile.visibility = View.VISIBLE
//                    binding.clickText.visibility = View.VISIBLE
//                    surveyImagesParts1!!.clear()

                    disableView();
                    isRunning = false;
                    stopService()

                    Utility.getInstance(activity).hideLoading()
                }
            })
        } catch (e: java.lang.Exception) {
            e.message
            Utility.getInstance(activity).hideLoading()

        }
    }

    private fun disableView() {
        binding.let {
            it.email.isEnabled = isUploading
            it.emailFrom.isEnabled = isUploading
            it.message.isEnabled = isUploading
            it.terms.isEnabled = isUploading
            it.buttonSecond.isEnabled = isUploading
            it.clickText.isEnabled = isUploading
            it.clickText1.isEnabled = isUploading
        }



        filesAdapter!!.setUploaded(!isUploading)
        isUploading = true
    }




    private fun stopService() {
        try {
            val intent1 = Intent(activity, SoundService::class.java)
            intent1.action = SoundService.ACTION.STOP_ACTION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //activity?.bindService(intent1,mServiceConnection, Context.BIND_AUTO_CREATE)
                activity?.startForegroundService(intent1)
            } else {
                activity?.stopService(intent1)
            }
        }catch ( e: Exception){
            e.stackTrace
        }
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBinder
            var mBoundService :SoundService  = myBinder.getService()

            mBoundService.setListener(this@SecondFragment)
            mServiceBound = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(isRunning)
          stopService()

    }


    override fun onResume() {
        super.onResume()

        if(isUploaded){
            isUploaded = false
            findNavController().navigate(R.id.action_SecondFragment_to_sucessFragment)

        }

    }

    override fun onProgressUpdate(current_percent: Int, totalPercent: Int, index: Int) {

        try {

            Log.i("check" + index, "--" + current_percent);
            if (mCurentPercent != current_percent) {

                mCurentPercent = current_percent;
                fileList!!.get(index).progress = current_percent;

                filesAdapter!!.notifyItemChanged(index)
               // _binding!!.filesRecycler!!.scrollToPosition(index)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onFailure() {

    }

    fun slideToLeft(view: View) {
        view.visibility= View.VISIBLE
        val slideAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        view.startAnimation(slideAnimation)
//        val animate = TranslateAnimation(0f, (500-view.width).toFloat(), 0f, 0f)
//        animate.duration = 500
//        animate.fillAfter = true
//        view.startAnimation(animate)

    }
}