package water.fvec;

import water.H2O;
import water.MemoryManager;
import water.util.UnsafeUtils;

/**
 * The empty-compression function, where data is in 'double's.
 * Can only be used locally (intentionally does not serialize).
 * Intended for temporary data which gets modified frequently.
 * Exposes data directly as double[]
 */
public final class C8DVolatileChunk extends Chunk {
  private transient double [] _ds;
  C8DVolatileChunk(double[] ds ) { _mem=new byte[0]; _start = -1; _len = ds.length; _ds = ds; }

  public double [] getValues(){return _ds;}
  @Override protected final long   at8_impl( int i ) {
    double res = _ds[i];
    if( Double.isNaN(res) ) throw new IllegalArgumentException("at8_abs but value is missing");
    return (long)res;
  }
  @Override protected final double   atd_impl( int i ) {
    return _ds[i] ;
  }
  @Override protected final boolean isNA_impl( int i ) { return Double.isNaN(_ds[i]); }
  @Override boolean set_impl(int idx, long l) {
    return false;
  }
  @Override boolean set_impl(int i, double d) {
    _ds[i] = d;
    return true;
  }
  @Override boolean set_impl(int i, float f ) {
    _ds[i] = f;
    return true;
  }
  public boolean isVolatile() {return true;}
  @Override boolean setNA_impl(int idx) { UnsafeUtils.set8d(_mem,(idx<<3),Double.NaN); return true; }
  @Override public NewChunk inflate_impl(NewChunk nc) {
    //nothing to inflate - just copy
    nc.alloc_doubles(_len);
    for( int i=0; i< _len; i++ )
      nc.doubles()[i] = _ds[i];
    nc.set_sparseLen(nc.set_len(_len));
    return nc;
  }
  @Override public final void initFromBytes () {throw H2O.unimpl("Volatile chunks should not be (de)serialized");}


  @Override
  public double [] getDoubles(double [] vals, int from, int to){
    throw H2O.unimpl();
  }

  /**
   * Dense bulk interface, fetch values from the given range
   * @param vals
   * @param from
   * @param to
   */
  @Override
  public double [] getDoubles(double [] vals, int from, int to, double NA){
    throw H2O.unimpl();
  }
  /**
   * Dense bulk interface, fetch values from the given ids
   * @param vals
   * @param ids
   */
  @Override
  public double [] getDoubles(double [] vals, int [] ids){
    throw H2O.unimpl();
  }

}