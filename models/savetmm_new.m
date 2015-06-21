load tMM_feat_39_mix_32;
whos
nmodels = size(params_tmm,2);
for i = 1:nmodels
    name = params_tmm(i).name;
    modelname = [name '.tmm'];
    fid = fopen(modelname, 'w');
    dim = size(params_tmm(i).mean,1);
    numTs = size(params_tmm(i).mean,2);
    means = params_tmm(i).mean';
    vars = params_tmm(i).covar;
    mixw = params_tmm(i).PI;
    nu = params_tmm(i).nu;
    logscalingconst = gammaln((nu+dim)/2)-0.5*sum(log(vars),2)-(dim/2)*log(pi*nu)-gammaln(nu/2);
    logscalingconst = logscalingconst';
    fprintf(fid,'#\n# Dimension of data (dim=scalar)\n');
    fprintf(fid,'%d\n',dim);
    fprintf(fid,'#\n# Number of student-t components in mixture (nTmm=scalar)\n');
    fprintf(fid,'%d\n',numTs);
    fprintf(fid,'#\n# eta values for all student-t component densities (nTmm x 1 array)\n');
    fprintf(fid,'%e ',nu);
    fprintf(fid,'\n');
    fprintf(fid,'#\n# Log scaling constants for all student-t component densities (nTmm x 1 array)\n');
    fprintf(fid,'%e ',logscalingconst);
    fprintf(fid,'\n');
    fprintf(fid,'#\n# mixture weights for all student-t component densities (nTmm x 1 array)\n');
    fprintf(fid,'%e ',mixw);
    fprintf(fid,'\n');
    fprintf(fid,'#\n# means for all student-t component densities (nTmm x dim matrix)\n');
    for j = 1:numTs
        fprintf(fid,'%e ',means(j,:));
        fprintf(fid,'\n');
    end
    fprintf(fid,'#\n# variances for all student-t component densities (nTmm x dim matrix}\n');
    for j = 1:numTs
        fprintf(fid,'%e ',vars(j,:));
        fprintf(fid,'\n');
    end
    fclose(fid);
    clear means vars mixw nu numTs dim logscalingconst;
end
