const shimmer =
  'before:absolute before:inset-0 before:-translate-x-full before:animate-[shimmer_2s_infinite] before:bg-gradient-to-r before:from-transparent before:via-white/60 before:to-transparent';

export function FruitListCardsSkeleton() {

  return (

      <div className={`${shimmer} relative overflow-hidden rounded-xl bg-gray-100 p-2 shadow-sm1`}>

         <div className="flex flex-wrap -mx-2">
            <div className="mb-4 px-2 w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/6">
               <div className="max-w-sm rounded overflow-hidden shadow-lg">
                  <div width={320} height={180} ></div>
                  <div className="px-6 py-4">
                     <div className="font-bold text-xl mb-2"></div>
                     <p className="text-gray-700 text-base">
                     </p>
                  </div>
                  <div className="px-6 pt-4 pb-2">
                     <button type="button" className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">
                     </button>
                     <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2"></span>
                  </div>
               </div>
            </div>
         </div>


      </div>
  );
}